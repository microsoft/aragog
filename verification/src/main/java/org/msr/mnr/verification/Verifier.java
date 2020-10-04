package org.msr.mnr.verification;

import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import org.msr.mnr.verification.dsfa.GlobalSFAParser;
import org.msr.mnr.verification.dsfa.LocalSFAParser;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.PacketDeSerializer;
import org.msr.mnr.verification.utils.MyPartitioner;
import org.msr.mnr.verification.utils.StringKeySelector;
import org.msr.mnr.verification.parser.ParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.InvalidParameterException;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

public class Verifier {
    private enum Mode {
        LOCAL_FILE, LOCAL_SOCKET, LOCAL_FOLDER, GLOBAL_KAFKA, STANDALONE_FILE, STANDALONE_FOLDER, UNDEFINED
    }

    public static <obj> void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ArgumentParser argparser = ArgumentParsers.newFor("GlobalVerifier").build()
                .defaultHelp(true).description("Create the verifier");
        argparser.addArgument("--config_dir").setDefault("out/").help("Config directory");
        argparser.addArgument("--out_path").setDefault("../out/").help("Output file");
        argparser.addArgument("--mode").setDefault("STANDALONE_FILE")
                .help("Mode: LOCAL_FILE, LOCAL_SOCKET, LOCAL_FOLDER, GLOBAL_KAFKA, STANDALONE_FILE, STANDALONE_FOLDER");
        argparser.addArgument("--input_file").setDefault("../notebook/38-38.0.001000.csv")
                .help("Input file needed for LOCAL or STANDALONE");
        argparser.addArgument("--input_folder").setDefault("out/globalInput/")
                .help("Input folder needed for global");
        argparser.addArgument("--broker_address").setDefault("localhost:9092").help("IP:Port,... for kafka broker");
        argparser.addArgument("--local_socket_ip").setDefault("localhost").help("IP for reading from a local socket");
        argparser.addArgument("--local_socket_port").setDefault("10000").help("Port for reading from a local socket");
        argparser.addArgument("--parser").setDefault("firewall").help("Select parser from generic or firewall");
        argparser.addArgument("--delimiter").setDefault("comma").help("Select parser delimiter from comma or semi-colon. Feel free to add more if neccessary");
        argparser.addArgument("--channel_per_invariant").setDefault("10").help("Channel Per Invariant");
        Namespace ns = argparser.parseArgs(args);


        // Get all execution parameters
        String configDir = ns.getString("config_dir");
        String formatFile = configDir + "packetformat.json";
        String outputPath = "file://" + System.getProperty("user.dir") + "/"
                + ns.getString("out_path");

        String delimiter = "";
        switch (ns.getString("delimiter")) {
        case "comma":
            delimiter = ",";
            break;
        case "semicolon":
            delimiter = ";";
            break;
        default:
            throw new RuntimeException("Unknown delimiter");
        }


        String parser = ns.getString("parser");


        Mode mode = Mode.UNDEFINED;
        switch (ns.getString("mode")) {
        case "LOCAL_FILE":
            mode = Mode.LOCAL_FILE;
            break;
        case "LOCAL_FOLDER":
            mode = Mode.LOCAL_FOLDER;
            break;
        case "LOCAL_SOCKET":
            mode = Mode.LOCAL_SOCKET;
            break;
        case "GLOBAL_KAFKA":
            mode = Mode.GLOBAL_KAFKA;
            break;
        case "STANDALONE_FOLDER":
            mode = Mode.STANDALONE_FOLDER;
            break;
        case "STANDALONE_FILE":
            mode = Mode.STANDALONE_FILE;
            break;
        default:
            throw new RuntimeException("Unknown Mode and Data Source: " + mode);
        }

        System.out.println("config directory: " + configDir);
        System.out.println("format file: " + formatFile);
        System.out.println("output folder path: " + outputPath);
        System.out.println("mode: " + mode);
        System.out.println("parser: " + parser);
        System.out.println("delimiter: " + delimiter);

        String inputPath;
        if (mode == Mode.LOCAL_FILE || mode == Mode.STANDALONE_FILE) {
            inputPath = "file://" + System.getProperty("user.dir") + "/"
                + ns.getString("input_file");
            System.out.println("input file path: " + inputPath);
        } else if (mode == Mode.LOCAL_FOLDER || mode == Mode.STANDALONE_FOLDER) {
            inputPath = ns.getString("input_folder");
            System.out.println("input folder path: " + inputPath);
        } else if (mode == Mode.LOCAL_SOCKET) {
            inputPath = ns.getString("local_socket_ip") + ":" + ns.getString("local_socket_port");
            System.out.println("Reading data from a socket: " + inputPath);
        } else if (mode == Mode.GLOBAL_KAFKA) {
            inputPath = ns.getString("broker_address");
            System.out.println("Reading data from kafka: " + inputPath);
        } else {
            throw new RuntimeException("Unknown Mode and Data Source: " + mode);
        }

        // Grab all state machine files
        File[] smFolder = new File(configDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*\\.sm\\.([0-9]+|g)");
            }
        });

        System.out.println("Files in smFolder: " +  Integer.toString(smFolder.length));
        HashMap<String, ArrayList<File>> smFiles = new HashMap<String, ArrayList<File>>();

        String brokers = inputPath;
        if (mode == Mode.LOCAL_SOCKET || mode == Mode.LOCAL_FOLDER || mode == Mode.LOCAL_FILE) {
            // Local state machines should get all .sm.g or .sm.[0-9]+ files
            for (File f : smFolder) {
                String filename = f.getName();
                String basename = filename.substring(0,
                        filename.lastIndexOf('.', filename.lastIndexOf('.') - 1));

                // System.out.println("Putting in smFiles: " + basename);
                if (!smFiles.containsKey(basename)) {
                    smFiles.put(basename, new ArrayList<File>());
                }
                smFiles.get(basename).add(f);
            }
        } else {
            // Global or standalone state machines only care about the global file
            for (File f : smFolder) {
                String filename = f.getName();
                if (filename.endsWith(".sm.g")) {
                    String basename = filename.substring(0, filename.length() - 5);
                    ArrayList<File> arr = new ArrayList<File>();
                    arr.add(f);

                    // System.out.println("Putting in smFiles: " + basename);
                    smFiles.put(basename, arr);
                }
            }
        }

        // Start assembling the Flink pipeline

        // Input
        DataStream<Packet> parsedPackets = null;
        HashMap<String, ArrayList<DataStream<Packet>>> parsedPacketsDict = null;
        
        HashMap<String, ArrayList<DataStream<Notification>>> finalOutputDict = new HashMap<String, ArrayList<DataStream<Notification>>>();

        MyPartitioner customPartitioner = new MyPartitioner();
        HashMap<String, StringKeySelector> sKeySelector = new HashMap<String, StringKeySelector>();

        if (mode == Mode.GLOBAL_KAFKA) {
            parsedPacketsDict = new HashMap<String, ArrayList<DataStream<Packet>>>();
            // Set Kafka properties
            Properties kafkaProps = new Properties();
            kafkaProps.setProperty("bootstrap.servers", brokers);
            kafkaProps.setProperty("auto.offset.reset", "earliest");
            for (Entry<String, ArrayList<File>> entry : smFiles.entrySet()) {
                for (int x = 0; x < Integer.parseInt(ns.getString("channel_per_invariant")); x++) {
                    String channelName = entry.getKey() + ".sm" + Integer.toString(x);
                    System.out.println("Channel name: " + channelName);
                    FlinkKafkaConsumer<String> kafka = new FlinkKafkaConsumer<>(channelName,
                                new SimpleStringSchema() {
                                    @Override
                                    public boolean isEndOfStream(String nextElement) {
                                        if (nextElement.contains("EndofStream")) {
                                            // throw new RuntimeException("End of Stream");  
                                            Date date = new Date();
                                            //This method returns the time in ms
                                            long timeMilli = date.getTime();
                                            System.out.println("End Time in ms: " + timeMilli + " with message: " + nextElement);     
                                            return true;
                                        } else { 
                                            return false;
                                        }
                                    }
                                }

                                ,kafkaProps);
                    kafka.setStartFromEarliest();
                    if (!(parsedPacketsDict.containsKey(entry.getKey()))) {

                        // System.out.println("Putting in parsedPacketsDict: " + entry.getKey());
                        parsedPacketsDict.put(entry.getKey(), new ArrayList<DataStream<Packet>>());
                    }
                    parsedPacketsDict.get(entry.getKey()).add(env.addSource(kafka).flatMap(ParserFactory.createNewParser(parser, formatFile, delimiter)));
                }
                
            }
        } else if (mode == Mode.STANDALONE_FILE || mode == Mode.LOCAL_FILE) {
            parsedPackets = env.readTextFile(inputPath).setParallelism(1)
                    .flatMap(ParserFactory.createNewParser(parser)).setParallelism(1);
        } else if (mode == Mode.STANDALONE_FOLDER || mode == Mode.LOCAL_FOLDER) {
            parsedPacketsDict = new HashMap<String, ArrayList<DataStream<Packet>>>();
            File[] inFolder = (new File(inputPath)).listFiles();
            System.out.println("Files in input Folder: " +  Integer.toString(inFolder.length));
            for (File f : inFolder) {
                String filename = f.getName();
                String invName = filename.substring(0,
                        filename.lastIndexOf('.', filename.lastIndexOf('.') - 1));

                String filePath = "file://" + System.getProperty("user.dir") + "/" + inputPath + filename;
                if (!(parsedPacketsDict.containsKey(invName))) {
                    // System.out.println("Putting in parsedPacketsDict: " + invName);
                    parsedPacketsDict.put(invName, new ArrayList<DataStream<Packet>>());
                }
                System.out.print("File: " + filePath + " on invariant: " + invName);
                
                // PacketKeySelector pKeySelector = new PacketKeySelector(filename);
                sKeySelector.put(filename,new StringKeySelector(filename));
                parsedPacketsDict.get(invName).add(env.readTextFile(filePath).setParallelism(1).partitionCustom(customPartitioner, sKeySelector.get(filename))
                    .flatMap(ParserFactory.createNewParser(parser, formatFile, delimiter)));
                // parsedPacketsDict.get(invName).add(env.readTextFile(filePath).setParallelism(1)
                //     .flatMap(ParserFactory.createNewParser(parser)));
                System.out.print(" Adding stream: ");
                System.out.println(parsedPacketsDict.get(invName).get(parsedPacketsDict.get(invName).size() - 1));
            }
            System.out.println("Out of loop");
        } else if (mode == Mode.LOCAL_SOCKET) {
            parsedPackets = env.socketTextStream(ns.getString("local_socket_ip") , Integer.valueOf(ns.getString("local_socket_port"))).setParallelism(1).flatMap(ParserFactory.createNewParser(parser, formatFile, delimiter));
        } else {
            throw new RuntimeException("Unknown Mode and Data Source: " + mode);
        }


        // Processing
        HashMap<String, FlinkKafkaProducer<Packet>> packetKafkaOutput = null;
        if (mode == Mode.LOCAL_FILE || mode == Mode.LOCAL_SOCKET || mode == Mode.LOCAL_FOLDER) {
            packetKafkaOutput = new HashMap<String, FlinkKafkaProducer<Packet>>();
        }

        for (Entry<String, ArrayList<File>> entry : smFiles.entrySet()) {
            if (!(finalOutputDict.containsKey(entry.getKey()))) {
                    // System.out.println("Putting in finalOutputDict: " + entry.getKey());
                    finalOutputDict.put(entry.getKey(), new ArrayList<DataStream<Notification>>());
            }

            if (mode == Mode.STANDALONE_FILE) {
                File f = entry.getValue().get(0);
                try (FileReader reader = new FileReader(f)) {
                    finalOutputDict.get(entry.getKey()).add(GlobalSFAParser
                            .makeDSFAProcessor(parsedPackets, reader, f.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (mode == Mode.LOCAL_FILE || mode == Mode.LOCAL_SOCKET) {
                finalOutputDict.get(entry.getKey()).add(LocalSFAParser
                            .makeDSFAProcessor(parsedPackets, entry.getKey(), entry.getValue()));
                // System.out.println("Putting in packetKafkaOutput: " + entry.getKey());
                packetKafkaOutput.put(entry.getKey(), new FlinkKafkaProducer<>(brokers,
                        entry.getKey(), new PacketDeSerializer()));

            } else if (mode == Mode.STANDALONE_FOLDER || mode == Mode.GLOBAL_KAFKA) {
                File f = entry.getValue().get(0); // Global SM file
                if (!parsedPacketsDict.containsKey(entry.getKey())) {
                    System.out.println("No input file for invariant: " + entry.getKey());
                    continue;
                }
                for (DataStream<Packet> currentStream : parsedPacketsDict.get(entry.getKey())) {
                    // System.out.print("currentStream:" );
                    // System.out.println(currentStream);
                    try (FileReader reader = new FileReader(f)) {
                        DataStream<Notification> tempVar = GlobalSFAParser
                            .makeDSFAProcessor(currentStream, reader, f.getName());
                        System.out.println("Creating Processor for: " + entry.getKey() + " invariant: " + f.getName() + " currentStream: " + currentStream.toString() + " with output: " + tempVar.toString());
                        finalOutputDict.get(entry.getKey()).add(tempVar);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (mode == Mode.LOCAL_FOLDER ) {
                // System.out.println("Putting in packetKafkaOutput: " + entry.getKey());
                packetKafkaOutput.put(entry.getKey(), new FlinkKafkaProducer<>(brokers,
                        entry.getKey(), new PacketDeSerializer()));
                for (DataStream<Packet> currentStream : parsedPacketsDict.get(entry.getKey())) {
                    finalOutputDict.get(entry.getKey()).add(LocalSFAParser.makeDSFAProcessor(currentStream,
                        entry.getKey(), entry.getValue()));
                }
            } else {
                throw new RuntimeException("Unknown Mode and Data Source: " + mode);
            }
        }


        // Output
        for (Entry<String, ArrayList<DataStream<Notification>>> entry : finalOutputDict.entrySet()) {
            if (mode == Mode.LOCAL_FILE || mode == Mode.LOCAL_FOLDER || mode == Mode.LOCAL_SOCKET) {
                for (DataStream<Notification> sfaOutput : entry.getValue()) {
                    sfaOutput.map(new MapFunction<Notification, Packet>() {
                            private static final long serialVersionUID = 6964249607698442117L;

                            @Override
                            public Packet map(Notification value) throws Exception {
                                return value.p;
                            }
                        }).addSink(packetKafkaOutput.get(entry.getKey()));

                }
            } else {
                int counter = 0;
                for (DataStream<Notification> sfaOutput : entry.getValue()) {
                    System.out.println("writing to file invariant: " + entry.getKey() + " for output stream: " + sfaOutput.toString());
                    sfaOutput.writeAsText(outputPath + entry.getKey() + Integer.toString(counter) + ".txt", WriteMode.OVERWRITE);
                    counter++;
                }
            }
        }
        

        //Getting the current date
        Date date = new Date();
        //This method returns the time in ms
        long timeMilli = date.getTime();
        System.out.println("Start Time in ms: " + timeMilli);
        // System.exit(0);
        System.out.println("Starting Flink execution...");
        env.execute("verification using stream with writing to file");
    }
}





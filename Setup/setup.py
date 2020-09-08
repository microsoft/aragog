import sys
import subprocess

def main():
    serverFile = open('servers.txt', 'r')
    serverList = list()

    for server in serverFile:
        serverList.append(server.strip())

    serverList = list(set(serverList))
    serverList.sort()
    if len(serverList) < 16:
        print('Less than 16 servers')
        sys.exit()

    serverList = list(serverList)
    serverDict = dict()
    internetInterface = dict()

    print("Getting interfaces")

    for server in serverList:
        command = server + ' ifconfig | grep -B1 128.* | grep -o "^\w*"'
        # print("command: ")
        # print(command)
        command = command.split(' ')
        test = subprocess.Popen(command, stdout=subprocess.PIPE)
        output = (test.communicate()[0]).decode('ascii').strip()
        # print(output)
        internetInterface[server] = output

        command = server + ' ifconfig | grep -B1 10.10 | grep -o "^\w*"'
        command = command.split(' ')
        test = subprocess.Popen(command, stdout=subprocess.PIPE)
        output = (test.communicate()[0]).decode('ascii').strip()
        # print(output)
        serverDict[server] = output


    print("Got all interfaces")
    print(serverDict)
    print(internetInterface)
    exit(0)

    internals = serverList[0:4]
    externals = serverList[4:8]
    firewalls = serverList[8:12]
    globalVerifier = serverList[12]

    print("Adding ip route rules and sending traffic files")

    for idx, server in enumerate(internals):
        command = server + ' "mkdir traffic; sudo ifconfig ' + serverDict[server] + ' 10.10.1.' + str(idx+1) + '; sudo ip route add 10.10.4.0/24 via 10.10.1.100 dev ' + serverDict[server] + '; sudo ip route add 10.10.5.0/24 via 10.10.1.50 dev ' + serverDict[server] + '"'
        command = server + ' "sudo rm -r traffic; mkdir traffic"'
        print(command)
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

        command = 'scp ../traffic/* ' + server.split(' ')[3] +':./traffic'
        print(command)
        # command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("traffic files are sent")
    exit(0)

    print("Setting IPs")

    for idx, server in enumerate(firewalls):
        command = server + ' ifconfig ' + serverDict[server] + ' 10.10.1.' + str(idx + 4)
        print(command)
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    exit(0)

    for i in [0,1]:
        command = externals[i] + ' ifconfig ' + serverDict[externals[i]] + ' 10.10.4.' + str(i)
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

        command = 'scp traffic/* ' + externals[i].split(' ')[3] +':./traffic'
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    for i in [2,3]:
        command = externals[i] + ' ifconfig ' + serverDict[externals[i]] + ' 10.10.5.' + str(i-2)
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

        command = 'scp traffic/* ' + externals[i].split(' ')[3] +':./traffic'
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("IPs are set")

    print("Installation starting")

    for server in serverList:
        command = server + ' sudo apt update && sudo apt install -y socat'
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("Update and socat done")

    print("Installing firewall stuff")

    for server in firewalls:
        command = server + ' echo iptables-persistent iptables-persistent/autosave_v4 boolean true | sudo debconf-set-selections && echo iptables-persistent iptables-persistent/autosave_v6 boolean true | sudo debconf-set-selections && sudo apt install -y iptables-persistent && sudo apt install -y conntrack conntrackd keepalived && git clone --recurse-submodules https://github.com/NofelYaseen/MBVerifier.git && sudo cp /usr/share/doc/conntrackd/examples/sync/primary-backup.sh /etc/conntrackd/ && cd MBVerifier/C++Verifier/ && ./setup.sh && make && sudo cp $HOME/MBVerifier/firewall/conntrackd.conf /etc/conntrackd/ && sudo cp $HOME/MBVerifier/firewall/keepalived.conf /etc/keepalived/ && sudo cp $HOME/MBVerifier/rules.v4 /etc/iptables/ && sudo sed -i -e "s/enp1s0/' + internetInterface[server] + '/g" /etc/iptables/rules.v4 && sudo sed -i -e s/interface enp1s0d1/interface ' + serverDict[firewalls[i]] + '/g /etc/conntrackd/conntrackd.conf '

        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("1st phase done - total 5")
    exit(0)

    for i in [0,1]:

        command = firewalls[i] + ' sudo sed -i -e s/10.10.X.100\/24 dev enp1s0d1/10.10.4.100\/24 dev ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/10.10.1.Y\/24 dev enp1s0d1/10.10.1.100\/24 dev ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/interface enp1s0d1/interface ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/virtual_router_id A/virtual_router_id 61/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/virtual_router_id B/virtual_router_id 62/g /etc/keepalived/keepalived.conf '
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    for i in [2,3]:
        command = firewalls[i] + ' sudo sed -i -e s/10.10.X.100\/24 dev enp1s0d1/10.10.5.100\/24 dev ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/10.10.1.Y\/24 dev enp1s0d1/10.10.1.50\/24 dev ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/interface enp1s0d1/interface ' + serverDict[firewalls[i]] + '/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/virtual_router_id A/virtual_router_id 71/g /etc/keepalived/keepalived.conf && sudo sed -i -e s/virtual_router_id B/virtual_router_id 72/g /etc/keepalived/keepalived.conf '
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("2nd phase done")

    for i in [0,2]:
        command = firewalls[i] + ' sudo sed -i -e s/priority P/priority 100/g /etc/keepalived/keepalived.conf '
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    for i in [1,3]:
        command = firewalls[i] + ' sudo sed -i -e s/priority P/priority 50/g /etc/keepalived/keepalived.conf '
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("3rd phase done")

    command = firewalls[0] + ' sudo sed -i -e s/IPv4_address 10.10.1.Y/IPv4_address 10.10.1.5g /etc/conntrackd/conntrackd.conf && sudo sed -i -e s/IPv4_Destination_Address 10.10.1.Z/IPv4_Destination_Address 10.10.1.4/g /etc/conntrackd/conntrackd.conf'
    command = command.split(' ')
    subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    command = firewalls[1] + ' sudo sed -i -e s/IPv4_address 10.10.1.Y/IPv4_address 10.10.1.4/g /etc/conntrackd/conntrackd.conf && sudo sed -i -e s/IPv4_Destination_Address 10.10.1.Z/IPv4_Destination_Address 10.10.1.5/g /etc/conntrackd/conntrackd.conf'
    command = command.split(' ')
    subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    command = firewalls[2] + ' sudo sed -i -e s/IPv4_address 10.10.1.Y/IPv4_address 10.10.1.6/g /etc/conntrackd/conntrackd.conf && sudo sed -i -e s/IPv4_Destination_Address 10.10.1.Z/IPv4_Destination_Address 10.10.1.7/g /etc/conntrackd/conntrackd.conf'
    command = command.split(' ')
    subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    command = firewalls[3] + ' sudo sed -i -e s/IPv4_address 10.10.1.Y/IPv4_address 10.10.1.7/g /etc/conntrackd/conntrackd.conf && sudo sed -i -e s/IPv4_Destination_Address 10.10.1.Z/IPv4_Destination_Address 10.10.1.6/g /etc/conntrackd/conntrackd.conf'
    command = command.split(' ')
    subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("4th phase done")

    for server in firewalls:
        command = firewalls[i] + ' sudo iptables-restore < /etc/iptables/rules.v4 && cd /etc/keepalived && sudo keepalived -l && cd /etc/conntrackd && sudo conntrackd -d'
        command = command.split(' ')
        subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("5th and final phase done")

    print("Installing global Verifier")

    command = globalVerifier + ' wget http://us.mirrors.quenda.co/apache/flink/flink-1.9.3/flink-1.9.3-bin-scala_2.11.tgz && tar xzf flink-1.9.3-bin-scala_2.11.tgz && git clone --recurse-submodules https://github.com/NofelYaseen/MBVerifier.git'
    # ' && cd MBVerifier/Setup && ./install.sh'
    command = command.split(' ')
    subprocess.Popen(command, shell=True,
             stdin=None, stdout=None, stderr=None, close_fds=True)

    print("Global verifier Installation done")
    print("Installation complete")

if __name__ == '__main__':
    main()
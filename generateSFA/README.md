# SFA generation

SFA generation has two dependencies: [symbolic automata](https://github.com/vinlnx/symbolicautomata), [z3](https://github.com/Z3Prover/z3/tree/796e2fd9eb8891a04a70fc0879a231a508479e13) and [antlr](https://www.antlr.org)

The major classes are:

## GenerateSFA

Arguments in generating the SFA.

|     Argument    |    options    |                                                                      |
|-----------------|:-------------:|:---------------------------------------------------------------------|
| -h, --help      |               | show this help message and exit                                      |
| --packet_format | PACKET_FORMAT | Packet format config file (default: ../config/SLB/packetformat.json) |
| --invar_file    | INVAR_FILE    | Input .invar file (default: ../config/SLB/primary_single.invar)      |
| --debug         |               | Packet format config file (default: false)                           |
| --out_dir       | OUT_DIR       | Output directory (default: ../out/)                                  |
| --examples      |               |  Run examples only (default: false)                                  |

This is the main class. It takes the configuration file that specifies the packet format, the invariant file and outputs the SFA in a form that can be accepted by the runtime verifiers.

Antlr is used to create the parse tree. SFA is created from parse tree using the class `InvariantVisitor`. SFA creation requires BooleanAlgebra logic, which is provided using `EventSolver` class. 

The SFA created is the global SFA. We then create the local SFAs using `GenerateLocalSFA` class. Finally, everything is written in `.sm` files. `.sm` files can be opened using any text editor.

## InvariantVisitor

The class recursive goes through the parse tree using antlr. It outputs the SFA after visiting the entire tree. Each function deals with specific node of the tree.

## EventSolver

It extends the `BooleanAlgebra` logic required to create the SFA. Please refer to the section 6.1 of the paper for details.

## GenerateLocalSFA

Takes the SFA and locations as input, and outputs the local SFA for each location.


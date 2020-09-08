# Instructions to run on cloudlab

Use Distributed Firewall Profile.  
Make 4 nodes external.
Make 4 nodes firewall (2 backup, 2 primary).
Rest are internal.

## Setup

### All nodes (including firewalls)

```
sudo apt update
sudo apt install tshark
```

### External nodes

Update IPs to make internal and external nodes in seperate networks.
There are two external networks:  
10.10.4.0  
10.10.5.0  

### Firewalls Only
```
sudo apt install iptables-persistent
sudo apt install conntrack conntrackd keepalived
git clone --recurse-submodules https://github.com/NofelYaseen/MBVerifier.git
sudo cp /usr/share/doc/conntrackd/examples/sync/primary-backup.sh /etc/conntrackd/
cd MBVerifier/C++Verifier/
./setup.sh
make
```

if git clone throws an error, edit `.gitmodules` and `.git/config` to use https address for z3 and symbolic automata rather than ssh.  

Make sure the paths are correct in `MBVerifier/C++Verifier/Makefile `.

If you get an error related to filesystem, change `#include<filesystem>` to `#include<experimental/filesystem>` and `namespace fs = std::filesystem;` to `namespace fs = std::experimental::filesystem;`

If happens because of older gcc version.

Copy conntrackd.conf to `/etc/conntrackd/` on each firewall  
Copy keepalived.conf to `/etc/keepalived/` on each firewall  
Copy rules.v4 to `/etc/iptables/` on each firewall  

```
sudo cp ~/MBVerifier/firewall/conntrackd.conf /etc/conntrackd/
sudo cp ~/MBVerifier/firewall/keepalived.conf /etc/keepalived/
cp ~/MBVerifier/rules.v4 /etc/iptables/
```
  
We need to update interface in each file. 
ethM stands for Middle interface. The interface going to replica of firewall  
ethI stands for Internal interface. The interface going to internal network  
ethE stands for External interface. The interface going to external network (internet)  

You can find interfaces by running `sudo ifconfig`. Check IPs at other nodes to find out, which one is external, internal or middle interface.  
  
In conntrackd.conf, also update IP IPv4 address of internet. This ip will be assigned to eth0 on firewalls. Also check the ip address id udp section.
In keepalived.conf, change priority (two places) of backup firewall to 50, and master to 100.  Also update the virutal ips according to the lan. There are total of 4 virutal routers. 2 on one set of primary/backup and 2 on the other. Make sure the IDs/IPs are correct.
In rules.v4, update the external_vip. Currently, it is set to 10.10.4.X and 10.10.5.X so might not need any change.   

Edit `run-verify.sh` and set the flink folder.

```
sudo iptables-restore < /etc/iptables/rules.v4
cd /etc/keepalived
sudo keepalived -l
cd /etc/conntrackd
sudo conntrackd -d
```

From the primary firewalls, you should be able to ping now.

### Global Verifier

```
wget http://us.mirrors.quenda.co/apache/flink/flink-1.9.3/flink-1.9.3-bin-scala_2.11.tgz
tar xzf flink-1.9.3-bin-scala_2.11.tgz
git clone --recurse-submodules https://github.com/NofelYaseen/MBVerifier.git
cd MBVerifier
./install.sh
```

### Internal nodes

`
sudo ip route add 10.10.4.0/24 via 10.10.1.100 dev enp3s0f0np0
sudo ip route add 10.10.5.0/24 via 10.10.1.50 dev enp3s0f0np0
`

You should be able start flows from internal network to external network, but not the other way around.

## Running

Compile invariants on your local machine and copy the compiled invariants to all the firewalls and global verifier in the folder SLBCode/out/


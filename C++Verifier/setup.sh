
sudo apt-get update
# sudo apt-get upgrade
sudo apt install -y libboost-all-dev
sudo apt install -y unzip
sudo apt install -y cmake
sudo apt install -y uuid-dev
sudo apt install -y clang++-10

cd

wget https://www.antlr.org/download/antlr-4.8-complete.jar
wget https://www.antlr.org/download/antlr4-cpp-runtime-4.8-source.zip

mkdir antl4cpp

unzip antlr4-cpp-runtime-4.8-source.zip -d antl4cpp

cd antl4cpp
mkdir build
mkdir run

cd build

cmake ..
DESTDIR=../run make install

cd ../run/usr/local/include
sudo cp -r antlr4-runtime /usr/local/include
cd ../lib
sudo cp * /usr/local/lib
sudo ldconfig

cd

git clone https://github.com/mfontanini/cppkafka.git
sudo apt install -y librdkafka-dev
cd cppkafka
mkdir build
cd build
cmake ..
make
sudo make install
sudo ldconfig


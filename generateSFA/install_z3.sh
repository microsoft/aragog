#! /bin/bash

cd z3
python scripts/mk_make.py --java
cd build
make; make install

cd ../../symbolicautomata/models
mvn clean install

cd ../..
mvn install:install-file -Dfile=z3/build/com.microsoft.z3.jar \
	      		 -DgroupId=com.microsoft \
	         	 -DartifactId=z3 \
		    	 -Dversion=4.8.7 \
		       	 -Dpackaging=jar \
		         -DgeneratePom=true

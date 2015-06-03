#classpath must be the path of the used JARs and the directory of the, separated with ':'.
#In this case, the package is named 'home.control' and contains the class Main (and others). 
#The package is placed in the dir 'bin'. Following this, the path to the package is 'bin'.
#This means the Main class is in the directory "/bin/home/control/Main.class".
#However: -classpath is <DirToJARs with wildcard>:<DirToPackage>
sudo java -classpath libs/*:bin home.control.Main

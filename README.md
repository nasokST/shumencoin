# shumencoin

To start the node use the following command in cmd:
java -Dserver.port=8081 -jar node-0.0.1-SNAPSHOT.jar -p=8081

To start mining upon a node use the following command on the same port as the node:
python shumnecoin_miner.py -p=8081

Notes:
- Throughout this project we use the different Java List implementations, where ArrayList have a mamixmum of Integer.MAX_VALUE elements that can hold. The LinkedList has no such limitation, but the way we get elements has to be implemented additionally.
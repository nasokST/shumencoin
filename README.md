# shumencoin

To start the node use the following command in cmd:<br />
java -Dserver.port=8081 -jar node-0.0.1-SNAPSHOT.jar -p=8081<br /><br />

To connect two nodes (temporary solution):<br />
POST: localhost:8080/peers/connect
{
	"url": "http://127.0.0.1:8081",
	"nodeId": "885569629946d4c01a9842e0bdff853e1eefdbe9e9487bee3b0aec38181c7aad",
	"chainId": 1,
	"notificationCallBack": true
}<br /><br />

To start mining upon a node use the following command on the same port as the node:<br />
python shumnecoin_miner.py -p=port_number -a=address

Notes:
- Throughout this project we use the different Java List implementations, where ArrayList have a mamixmum of Integer.MAX_VALUE elements that can hold. The LinkedList has no such limitation, but the way we get elements has to be implemented additionally.
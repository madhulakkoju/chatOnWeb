<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HELP CHAT</title>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>


var wsocket;
var serviceLocation = "ws://localhost:8080/chatOnWeb/helpChat/";
var $nickName;
var $message;
var $chatWindow;
var $alert;
var room = '';
var user = '';

var messages =["abc","dfsdf","sdsdf","sfsdf","ffsfsd","dfsdfsdf","sdfgsdfsdf","dfSDfsd","dfsdssd ","fdfsdg"]
var senderEmail ="ADMIN_USER";

$(document).ready(function () {
   
    $message = $('#message');
    $chatWindow = $('#showChat');
    $alert = $('#alert');

    connectToChatServer();

    $('#do-chat').submit(function (evt) {
        evt.preventDefault();
        sendMessage()
    });
});

function connectToChatServer()
{
	wsocket = new WebSocket(constructURI(serviceLocation,senderEmail));
	
	wsocket.onerror = onConnectionError;
    wsocket.onmessage = onMessageReceived;
    console.log("Done");
}

function onMessageReceived(evt)
{
	var message = JSON.parse(evt.data); // sender ::: messageBody
	console.log(message);

	var table = document.getElementById("showChat");
	
	var tr=document.createElement('TR');
	var td = document.createElement('TD')
	td.appendChild(document.createTextNode(message.Sender));
	tr.appendChild(td);
	
	td = document.createElement('TD')
	td.appendChild(document.createTextNode("-"))
	tr.appendChild(td);
	
	td = document.createElement('TD')
	td.appendChild(document.createTextNode(message.MessageBody));
	tr.appendChild(td);
	table.appendChild(tr);
	scrollDown();
}

function onConnectionError(evt)
{
	$alert.append($(evt));
}

function sendMessage()
{
	console.log("Send Message");
	
	msg = new Object();
	msg.MessageBody = document.getElementById("message").value;
	if(msg.MessageBody=="")
		return;
	msg.receiver = "ADMIN_USER";
	msg.Sender =document.getElementById("email").value;
	
    document.getElementById("message").value="";
	
	//messages.push(msg);
	// send the msg object to json to server for addition of the Data to database
	
	updateChat(msg);
	msg.receiver = msg.Sender;
	// Construct message to send to server
    var mssg = JSON.stringify(msg);
    wsocket.send(mssg);
	console.log(mssg);
    // Put back focus
    $message.val('').focus();
    
    console.log("Sent Message");
}


function constructURI(serviceLocation,user)
{
	return serviceLocation+user;
}



/**
 * 
 * The Chat Building page related function calls 
 *
 */


function buildChat()
{
	var table = document.getElementById("showChat");
	
	for(i=0;i<messages.length;i++)
	{
		var tr=document.createElement('TR');
		var td = document.createElement('TD')
		td.appendChild(document.createTextNode(messages[i]))
		tr.appendChild(td)
		table.appendChild(tr);
	}
	scrollDown();
}


function updateChat(message)
{
	var table = document.getElementById("showChat");
	
	var tr=document.createElement('TR');
	var td = document.createElement('TD')
	td.appendChild(document.createTextNode(message.Sender))
	tr.appendChild(td)
	
	td = document.createElement('TD')
	td.appendChild(document.createTextNode(message.receiver))
	tr.appendChild(td)
	
	td = document.createElement('TD')
	td.appendChild(document.createTextNode(message.MessageBody))
	tr.appendChild(td)
	
	table.appendChild(tr);
	
	scrollDown();
}
	


function scrollDown()
{
	var objDiv = document.getElementById("Chat");
    objDiv.scrollTop = objDiv.scrollHeight;
}





</script>

</head>
<body >

<div class ="helpChatBox" style ="width:5000px;height:300px">
<div class = "User Data" style ="background-color:black;color:white;padding:10px;text-align:center;" >
<h1>Help Chat</h1>
</div>

<div class = "Chat" id="Chat" style ="overflow:hidden;padding:5px;text-align:center;height:200px;overflow-y:scroll;">
<table id = "showChat" >
<th id = "showChatHeader"></th>
</table>
</div>


<form id = "do-chat">
<input type="text" id="email" >
<input type="text" id="message" style="padding: 5px;width: 500px;height:60px">
<input type="submit" value="Send"/style="padding: 5px;width: 70px;height:60px">
</form>
</div>




</body>
</html>
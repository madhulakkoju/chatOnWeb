
var wsocket;
var serviceLocation = "ws://localhost:8080/chatOnWeb/helpChat/";
var $nickName;
var $message;
var $chatWindow;
var $alert;
var room = '';
var user = '';

var messages =["abc","dfsdf","sdsdf","sfsdf","ffsfsd","dfsdfsdf","sdfgsdfsdf","dfSDfsd","dfsdssd ","fdfsdg"]
var senderEmail ="Madhu@gmail.com";

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
	var msg = JSON.parse(evt.data)
	// add to frontend
	return msg;
}

function onConnectionError(evt)
{
	$alert.append($(evt));
}

function sendMessage()
{
	// Construct message to send to server
    var msg = '{"MessageBody":"' + $message.val() + '", "Sender":"' + user + '", "Time":"' + '"}';
    wsocket.send(msg);

    // Put back focus
    $message.val('').focus();
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
	td.appendChild(document.createTextNode(message.MessageBody))
	tr.appendChild(td)
	
	table.appendChild(tr);
	
	scrollDown();
}
	


function sendMessage()
{
	msg = new Object();
	msg.Sender = senderEmail;
	msg.MessageBody = document.getElementById("text").value;
	msg.dt = new Date();
	document.getElementById("text").value="";
	
	messages.push(msg);
	// send the msg object to json to server for addition of the Data to database

	updateChat(msg);
}

function scrollDown()
{
	var objDiv = document.getElementById("Chat");
    objDiv.scrollTop = objDiv.scrollHeight;
}






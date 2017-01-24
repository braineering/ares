'use strict';

/*
 * It allows to download the file of with text from @response_body
 * 
 * @return {Boolean} true if the operation is successful, false if not
 */

function DownloadFile() 
{
  var type = document.getElementById('response_title').textContent;
  var data = document.getElementById('response_body').textContent;
  
  if(data == "" || type == "")
  	return false;

  var blob = new Blob([data], {type: "text/plain;charset=utf-8"});

  if(type=="Configuration:")
  	saveAs(blob, "config.yaml");
  else if(type=="Command:")
  	saveAs(blob, "command.json");
  else if(type=="Initialization:")
    saveAs(blob, "init.json");
  else
  	return;

  return true;
};
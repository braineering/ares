'use strict';


function SubmitFile() 
{
  var rtype = document.getElementById('response_title').textContent;
  var rdata = document.getElementById('response_body').textContent;

  if(rtype == "Initialization:"){

    var value = rdata || '{}';
    try {
      var initialization = JSON.parse(value);
    } catch (e) {
      alert(e);
      return;
    }

    $.ajax({
      type: 'POST',
      url: '/init',
      dataType: 'json',
      contentType: 'application/json',
      data: JSON.stringify(initialization)
    });

  }else if(rtype == "Command:"){

    var value = rdata || '{}';
    try {
      var command = JSON.parse(value);
    } catch (e) {
      alert(e);
      return;
    }

    $.ajax({
      type: 'POST',
      url: '/command',
      dataType: 'json',
      contentType: 'application/json',
      data: JSON.stringify(command)
    });

  }else{
    //failure
    return;
  }
	
};
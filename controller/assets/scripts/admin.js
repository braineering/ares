$(document).ready(function(){

  function fnSubmitInitialization () {
    var value = $('#initialization').val() || '{}';
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
  }

  function fnSubmitCommand () {
    var value = $('#command').val() || '{}';
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
  }

  $('#submitInitialization').click(fnSubmitInitialization);

  $('#submitCommand').click(fnSubmitCommand);

});



'use strict';

/*
 * This function adds a new target in the "Attack Table." 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
var AddTargetFormSubmit = function AddTargetFormSubmit(event) {

  event.preventDefault();

  var data = formToJSON(add_target_form.elements);

  var table = document.getElementById("targets_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  var cell3 = row.insertCell(2);
  var cell4 = row.insertCell(3);
  var cell5 = row.insertCell(4);
  var cell6 = row.insertCell(5);

  if(data["target"])
    cell1.innerHTML = data["target"];
  else
    cell1.innerHTML = "none";
  
  if(data["method"])
    cell2.innerHTML = data["method"];
  else
    cell2.innerHTML = "none";

  if(data["proxy"])
    cell3.innerHTML = data["proxy"];
  else
    cell3.innerHTML = "none";

  if(data["user-agent"])
    cell4.innerHTML = data["user-agent"];
  else
    cell4.innerHTML = "none";

  if(data["executions"])
    cell5.innerHTML = data["executions"];
  else
    cell5.innerHTML = "null";

  if(data["period"] && data["period-option"]){
    var p = data["period"].split(';');
    cell6.innerHTML = p[0]+"-"+p[1]+":"+data["period-option"];  
  }
  else
    cell6.innerHTML = "null";  

  $('#add_target_modal').modal('toggle');

};

var add_target_form = document.getElementById('add-target-form');
add_target_form.addEventListener('submit', AddTargetFormSubmit);
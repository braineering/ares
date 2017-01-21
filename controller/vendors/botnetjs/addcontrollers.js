'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
var AddControllersFormSubmit = function AddControllersFormSubmit(event) {

  event.preventDefault();

  var data = formToJSON(add_controllers_form.elements);

  console.log(data["controller_init"]);
  console.log(data["controller_cmd"]);
  console.log(data["controller_log"]);
  
  var table = document.getElementById("controllers_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  var cell3 = row.insertCell(2);
  
  if(data["controller_init"])
    cell1.innerHTML = data["controller_init"];
  else
    cell1.innerHTML = "none";
  
  if(data["controller_cmd"])
    cell2.innerHTML = data["controller_cmd"];
  else
    cell2.innerHTML = "none";

  if(data["controller_log"])
    cell3.innerHTML = data["controller_log"];
  else
    cell3.innerHTML = "none";

  $('#add_controllers_modal').modal('toggle');

  return true;
};

var add_controllers_form = document.getElementById('add-controllers-form');
add_controllers_form.addEventListener('submit', AddControllersFormSubmit);
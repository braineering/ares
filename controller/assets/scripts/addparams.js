'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddParamsFormSubmit() {

  event.preventDefault();
  var add_params_form = document.getElementById('add-params-form');
  var data = formToJSON(add_params_form.elements);

  console.log(data["param_name"]);
  console.log(data["param_value"]);
  
  var table = document.getElementById("params_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  
  if(data["param_name"])
    cell1.innerHTML = data["param_name"];
  else
    cell1.innerHTML = "none";
  
  if(data["param_value"])
    cell2.innerHTML = data["param_value"];
  else
    cell2.innerHTML = "none";

  $('#add_params_modal').modal('toggle');
  $('#add_target_modal').modal('show');

  return true;
};
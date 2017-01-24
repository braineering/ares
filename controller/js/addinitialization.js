'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddInitializationFormSubmit() {

  event.preventDefault();
  
  var add_initialization_form = document.getElementById('add-initialization-form');
  var data = formToJSON(add_initialization_form.elements);
  
  var table = document.getElementById("initialization_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  
  if(data["initialization_name"])
    cell1.innerHTML = data["initialization_name"];
  else
    cell1.innerHTML = "none";
  
  if(data["initialization_value"])
    cell2.innerHTML = data["initialization_value"];
  else
    cell2.innerHTML = "none";

  $('#add_initialization_modal').modal('toggle');

  return true;
};
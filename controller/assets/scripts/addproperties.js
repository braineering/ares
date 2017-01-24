'use strict';

/*
 * This function adds a new set of controllers in the "Controllers Table". 
 * The data comes from the modal insertion of the new target.
 *
 + @return {Boolean} true if the operation is successful, false if not
 */
function AddPropertiesFormSubmit() {

  event.preventDefault();

  var add_properties_form = document.getElementById('add-properties-form');
  var data = formToJSON(add_properties_form.elements);

  var table = document.getElementById("properties_table");
  var row = table.insertRow(1);
  var cell1 = row.insertCell(0);
  var cell2 = row.insertCell(1);
  
  if(data["property_name"])
    cell1.innerHTML = data["property_name"];
  else
    cell1.innerHTML = "none";
  
  if(data["property_value"])
    cell2.innerHTML = data["property_value"];
  else
    cell2.innerHTML = "none";

  $('#add_properties_modal').modal('toggle');

  return true;
};

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Kind of a Jean Cocteau touch, isn\'t it?', 'Khairei, deerslayers!', 'Beauty is harsh.',
      'I\'m late for an appointment.', 'Duty, piety, loyalty, sacrifice.', 'It\'s just such a cowardly thing to have done.'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Written with help from https://www.w3schools.com/howto/howto_html_include.asp
function createNavigation() {
  // Loop through a collection of all HTML elements. 
  const total = document.getElementsByTagName("*");
  for (const element of total) {
    // Search for elements with a certain atrribute.
    const file = element.getAttribute("w3-include-html");
    let xhttp;
    if (file) {
      // Make an HTTP request using the attribute value as the file name
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {  
        if (this.readyState === 4) {
          if (this.status === 200) {element.innerHTML = this.responseText;}
          if (this.status === 404) {element.innerHTML = "Page not found.";}
          // Remove the attribute, and call this function once more
          element.removeAttribute("w3-include-html");
          createNavigation();
        }
      }
      xhttp.open("GET", file, /* async = */ true);
      xhttp.send();
      // Exit the function
      return;
    }
  }
}

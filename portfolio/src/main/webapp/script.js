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
 * Adds a quote from The secret History to the page.
 */
function addRandomQuote() {
  const quotes = [
    'Kind of a Jean Cocteau touch, isn\'t it?', 'Khairei, deerslayers!',
    'Beauty is harsh.', 'I\'m late for an appointment.',
    'Duty, piety, loyalty, sacrifice.',
    'It\'s just such a cowardly thing to have done.',
    'That information is classified, I\'m afraid.', 'Oh, well, then.',
    'But you\'re not very happy where you are, either.',
    'If we leave now we can be in Montreal by dark.',
    'He\'s your friend. So am I.', 'Are we insane?',
    'I\'m glad you didn\'t go.', 'Do you feel afraid a lot?',
    'Did you do your Greek for today?', 'Just once I\'d like to...'
  ];

  // Pick a random greeting.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quotesContainer = document.getElementById('quotes-container');
  quotesContainer.innerText = quote;
}

/**
 * Generates a URL for a random image in the images directory and adds an img
 * element with that URL to the page.
 */
function randomizeImage() {
  const imageIndex = Math.floor(Math.random() * 18) + 1;
  const imageUrl = 'images/cats/cats-' + imageIndex + '.jpg';

  const imageElement = document.getElementById('cat-photo');
  imageElement.src = imageUrl;
  imageElement.setAttribute('height', '500');
}

/**
 * Adds navigation HTML snippet to the other HTML files.
 * Written with help from https://www.w3schools.com/howto/howto_html_include.asp
 */
function createNavigation() {
  // Loop through a collection of all HTML elements.
  const total = document.getElementsByTagName('*');
  for (const element of total) {
    // Search for elements with a certain atrribute.
    const file = element.getAttribute('w3-include-html');
    let xhttp;
    if (file) {
      // Make an HTTP request using the attribute value as the file name
      xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState === 4) {
          if (this.status === 200) {
            element.innerHTML = this.responseText;
          }
          if (this.status === 404) {
            element.innerHTML = 'Page not found.';
          }
          // Remove the attribute, and call this function once more
          element.removeAttribute('w3-include-html');
          createNavigation();
        }
      } 
      xhttp.open('GET', file, /* async = */ true);
      xhttp.send();
      // Exit the function
      return;
    }
  }
}

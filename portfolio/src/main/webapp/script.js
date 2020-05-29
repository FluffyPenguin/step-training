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


const arrEqual = (arr1, arr2) => { //returns true if arr1 and arr2 have the same elements.
  if (arr1.length != arr2.length) {
    return false;
  }
  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] != arr2[i]) {
      return false;
    }
  }
  return true;
}

let subscribed = false;
let liked = false;
//easter eggs
const konami = ["ArrowUp", "ArrowUp", "ArrowDown", "ArrowDown", "ArrowLeft", "ArrowRight", "ArrowLeft", "ArrowRight", "KeyA", "KeyB"];
const konamiCheck = [];
const penguin = ["KeyP", "KeyE", "KeyN", "KeyG", "KeyU", "KeyI", "KeyN"];
const penguinCheck = [];

//activate correct header button
// const topNavBtn = document.getElementById("index.html");
// topNavBtn.className = "active";

const pikachuMeme = document.querySelector("#pikachuMeme");
pikachuMeme.style.opacity = "1";
pikachuMeme.addEventListener("click", (event) => {
  if (parseFloat(pikachuMeme.style.opacity) == 1) {
    alert("Don't click my pikachu");
  }
  if (parseFloat(pikachuMeme.style.opacity) > 0) {
    pikachuMeme.style.opacity = parseFloat(pikachuMeme.style.opacity) - 0.1;
  } else{
    alert("Look what you've done now");
  }
});

//like button event listeners
const likeButton = document.querySelector("#likeButton");
likeButton.addEventListener("click", (event) => {
  likeButton.innerHTML = "Liked!";
  likeButton.style.backgroundColor = "green";
  liked = true;
});

likeButton.addEventListener("mouseover", (e) => {
  if (liked) return;
  likeButton.style.backgroundColor = "lightgreen";
});

likeButton.addEventListener("mouseout", (e) => {
  if (liked) return;
  likeButton.style.backgroundColor = "white";

});
//subscribe button event listeners
//-------------------------
const subscribeButton = document.querySelector("#subscribeButton");
subscribeButton.addEventListener("click", (event) => {
  subscribeButton.innerHTML = "Subscribed!";
  subscribeButton.style.backgroundColor = "green";
  subscribed = true;
});

subscribeButton.addEventListener("mouseover", (e) => {
  if (subscribed) return;
  subscribeButton.style.backgroundColor = "lightgreen";
});

subscribeButton.addEventListener("mouseout", (e) => {
  if (subscribed) return;
  subscribeButton.style.backgroundColor = "white";

});
//END subscribe button event listeners

//make the title random color when clicked
const mainTitle = document.querySelector("#mainTitle");
mainTitle.addEventListener("click", (e) => {
  mainTitle.style.color = `rgb(${Math.random()*256},${Math.random()*256},${Math.random()*256})`;
  //make all the h2's change colors too
  const h2s = document.querySelectorAll("h2");
  h2s.forEach((h2Obj) => {
    h2Obj.style.color = mainTitle.style.color;
  })
});


//secret Konami Code ;o
const checkKonami = (e) => {
  konamiCheck.push(e.code);
  if (konamiCheck.length > 10) {
    konamiCheck.shift(); //removes the first elem of array
  }
  if (arrEqual(konami, konamiCheck)) { //KONAMI CODE ENTERED :OO
    //mainTitle.style.color = "red";
    const easterEggPikachu = document.querySelector("#easterEggPikachu");
    easterEggPikachu.style.height = "100%";
    easterEggPikachu.style.width = "100%";
    easterEggPikachu.style.display = "block"; //make it visible

    //frogger!
    // setTimeout(() => {
    //   window.location.href = "frogger.html";
    // }, 500);
  }
  //console.log(konamiCheck);
}; //end check Konami

//penguin easter egg
const checkPenguin = (e) => {
  penguinCheck.push(e.code);
  if (penguinCheck.length > 7) {
    penguinCheck.shift();
  }
  if (arrEqual(penguin, penguinCheck)) {
    const easterEggPenguin = document.querySelector("#easterEggPenguin");
    easterEggPenguin.style.height = "100%";
    easterEggPenguin.style.width = "100%";
    easterEggPenguin.style.display = "block"; //make it visible
  }
};
const body = document.querySelector("body");
window.addEventListener("keydown", (e) => {
  checkKonami(e);
  checkPenguin(e);

});

//Fetch
const getComments = async () => {
  
  const numComments = document.querySelector("#numComments").value;
  const response = await fetch('/data-comments?numComments=' + numComments);
  
  const comments = await response.json();
  const commentDiv = document.getElementById('commentDiv');
  commentDiv.innerText = "";
  comments.forEach(comment => commentDiv.innerText += comment + "\n");
}



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


//activate correct header button
// const topNavBtn = document.getElementById("index.html");
// topNavBtn.className = "active";


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

//Fetch
const getComments = async () => {
  
  const numComments = document.querySelector("#maxNumComments").value;
  const response = await fetch('/data-comments?maxNumComments=' + numComments);
  
  const comments = await response.json();
  const commentDiv = document.getElementById('commentDiv');
  commentDiv.innerText = "";
  comments.forEach(comment => commentDiv.innerText += comment + "\n");
}

const commentText = document.querySelector("#commentText");
const postComment = async() => {
  let comment = commentText.value;
  commentText.value = "";
  await fetch("/data-comments", {
      method: "POST",
      body: new URLSearchParams({"commentText" : comment})
    });
  commentDiv.innerText = comment + "\n" + commentDiv.innerText;
}


const authDiv = document.querySelector('#authDiv');
const loadAuthHTML = async() => {
  const response = await fetch('/auth');
  const authHTML = await response.text();
  authDiv.innerHTML = authHTML;
}

const loadDynamicContent = async() => {
	loadAuthHTML();
  getComments();
}


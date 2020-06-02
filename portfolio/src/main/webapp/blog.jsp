<!DOCTYPE html>
<html>

<head>
  <title>Alex Kim's Blog</title>
  <link rel="stylesheet" href="style.css"/>
  <%@include file="head.html" %>


</head>

<body onload="getComments()">
  <%@include file="topnav.html" %>

  <div id="home" class="main">
    <h1 id="mainTitle">Alex Kim</h1>
    <p> WIP </p>

    <!-- <button onclick="getResponse()" id="helloTxt"> Get Comments </button> -->
    <form id="commentForm" onsubmit="postComment(); return false">
      <label for="commentText"> Post a comment! </label>
      <br/>
      <textarea required id="commentText" name="commentText"></textarea>
      <input type="submit" value="Post" />
    </form>
    <label for="maxNumComments"> # Comments to Display </label>
    <input onchange="getComments()" type="number" id="maxNumComments" name="maxNumComments" min="1" max="10000" required value=100>
    <br/>
    <div id="commentDiv"> </div>
  
</div id ="home">





<script src="script.js"> </script>
<!-- link at the bottom to reduce load time -->
</body>
</html>
<!DOCTYPE html>
<html>

<head>
  <title>Alex Kim's Blog</title>
  <link rel="stylesheet" href="blog.css"/>
  <%@include file="head.html" %>


</head>

<body onload="loadDynamicContent()">
  <%@include file="topnav.html" %>

  <div id="home" class="main">
    <h1 id="mainTitle">Alex Kim</h1>
    <div class="blogPost">
      <h2> Blog Post Title </h2>
      	<p> 
        	This is an example blog post. Currently this is hardcoded. 
          Hello there if you are reading this far! From now on, this text won't make much sense!
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt 
          ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco 
          laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in 
          voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat
          non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." 
        </p>
    </div>

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





<script src="blog.js"> </script>
<!-- link at the bottom to reduce load time -->
</body>
</html>
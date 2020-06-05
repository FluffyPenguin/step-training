<!DOCTYPE html>
<html>

<head>
  <title>Create your account</title>
  <link rel="stylesheet" href="static/styles/blog.css"/>
  <%@include file="head.html" %>


</head>

<body onload="loadDynamicContent()">
  <%@include file="topnav.html" %>

  <div id="home" class="main">
    <h1 id="mainTitle">Create your account</h1>
    <form id="createAccountForm" method="POST" action="/createProfile">
      <label for="username">Username</label>
      <textarea required id="username" name="username"></textarea>
      <input id="createAccount" type="submit" value="Create!" />
    </form>
    <br/>
    
    
  
</div id ="home">





<script src="static/scripts/blog.js"> </script>
<!-- link at the bottom to reduce load time -->
</body>
</html>
<!DOCTYPE html>
<html>

<head>
  <title>Alex Kim</title>
  <link rel="stylesheet" href="style.css"/>
  <%@include file="head.html" %>


</head>

<body onload="loadDynamicContent()">
  <%@include file="topnav.html" %>

  <img id="easterEggPikachu"
    src="https://i.kym-cdn.com/entries/icons/original/000/027/475/Screen_Shot_2018-10-25_at_11.02.15_AM.png"
    alt="Easter Egg Surprised Pikachu"
  />
  <img id="easterEggPenguin"
    src="https://nheilke.com/blog/wp-content/uploads/2011/12/IMG_9120.jpg"
    alt="Easter Egg Penguin"
  />

  <div id="home" class="main">
    <h1 id="mainTitle">Alex Kim</h1>
    <div class="divider"></div>

    <div id = "aboutme" class="rowFlexbox">
      <div id ="picAndLinks">
        <img
          id="selfPicture"
          src = "imgs/self.jpg"
          alt = "Picture of myself"
        />
        <div id="links" class="rowFlexbox">
          <a id="githubLink" class="logo"
            href="https://github.com/FluffyPenguin">
            <img
              id="githubLogo"
              class="logo"
              src="imgs/GitHub.png"
              alt = "Github"
            />
          </a>
          <a id="linkedInLink" class="logo"
            href="https://www.linkedin.com/in/alexkimt/">
            <img
              id="inLogo"
              class="logo"
              src="imgs/LI.png"
              alt = "LinkedIn"
            />
          </a>
        </div>
      </div>
      <div id="aboutMeText">
        <p>
          Hello! I'm Alex, a rising second year CS major attending Georgia Tech! I was born and raised all my life in the suburbs of Atlanta, Georgia.
        </p>
        <p>
          I'm interested in robotics, new tech, artificial intelligence, and much more!
        </p>
      </div>
    </div>
    
    <h2> Some Stuff I Like </h2>
    <ul >
      <li> Penguins </li>
      <li> Table Tennis </li>
      <li> Piano </li>
      <li> Classical and Pop Music </li>
      <li> Food </li>
      <li> Memes (check out <a rel="noopener noreferrer" href="https://metwoapp.appspot.com/" target="_blank" >this cool meme sharing site I made!</a>) </li>

      <li> Google </li>
    </ul>

    <h2> My Favorite Meme </h2>

    <img
      id = pikachuMeme
      src = "https://pics.me.me/when-you-see-a-surprised-pikachu-meme-in-4k-woww-37497086.png"
      alt = "pikachu meme"
    />
    <br/>
  	<br/>
</div id ="home">





<script src="index.js"> </script>
<!-- link at the bottom to reduce load time -->
</body>
</html>
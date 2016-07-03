ngGallery
=========

Angular is only dependency (no jQuery).

#### Example 

Check out [the live demo](http://demo.jankuri.com/ngGallery/)

Install
-------

#### With bower:

    $ bower install ngGallery-buras

#### Example Configuration

#### app.js
```js
angular.module('app', ['jkuri.gallery']).
  controller('Ctrl', function($scope, $document) {
    self.images = [
      {Thumbnail: 'images/thumbs/1.jpg', Image: 'images/1.jpg'},
		  {Thumbnail: 'images/thumbs/2.jpg', Image: 'images/2.jpg'},
		  {Thumbnail: 'images/thumbs/3.jpg', Image: 'images/3.jpg'},
		  {Thumbnail: 'images/thumbs/4.jpg', Image: 'images/4.jpg'},
		  {Thumbnail: 'images/thumbs/5.jpg', Image: 'images/5.jpg'},
		  {Thumbnail: 'images/thumbs/6.jpg', Image: 'images/6.jpg'}
    ];
  }
);
```

#### index.html
```html
<html ng-app="app">
<head>
	<title>ngGallery</title>
	<link rel="stylesheet" type="text/css" href="src/css/screen.css">
	<link rel="stylesheet" type="text/css" href="src/css/ngGallery.css">
</head>
<body ng-controller="Ctrl as ctrl">

<div class="content">
	<ng-gallery images="ctrl.images"></ng-gallery>
</div>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.0/angular.min.js"></script>
<script type="text/javascript" src="src/js/ngGallery.js"></script>
<script type="text/javascript" src="app.js"></script>
</body>
</html>
```

That's all. 

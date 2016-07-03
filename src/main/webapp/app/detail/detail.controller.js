(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('DetailController', DetailController);


    DetailController.$inject = ['LoginService','Principal','$scope', '$rootScope', '$stateParams', 'entity', 'InstanceReview', 'InstanceTur', 'Picture', 'InstanceContact', 'InstanceFacility', 'InstanceActivity', 'InstanceRoomType', 'InstancePolicy', 'InstanceInfo', 'Booking', 'InstanceTurType', 'Info'];

    function DetailController (LoginService, Principal, $scope, $rootScope, $stateParams, entity, InstanceReview, InstanceTur, Picture, InstanceContact, InstanceFacility, InstanceActivity, InstanceRoomType, InstancePolicy, InstanceInfo, Booking, InstanceTurTyp, Info) {
        var vm = this;
        vm.instanceTur = entity;
		    vm.pictures = Picture.query();
		    vm.instanceroomtypes = InstanceRoomType.query();
        vm.activities = InstanceActivity.query();
        vm.info = Info.query();
        vm.reviews = InstanceReview.query();

        vm.load = function (id) {
            InstanceTur.get({id: id}, function(result) {
                vm.instanceTur = result;
            });
        };

		vm.myInterval = 3000;

	    vm.images = [
	        		{thumb: '/content/images/images/thumbs/1.jpg', img: '/content/images/images/1.jpg', description: 'Image 1'},
			        {thumb: 'content/images/images/thumbs/2.jpg', img: 'content/images/images/2.jpg', description: 'Image 2'},
			        {thumb: 'content/images/images/thumbs/3.jpg', img: 'content/images/images/3.jpg', description: 'Image 3'},
			        {thumb: 'content/images/images/thumbs/3.jpg', img: 'content/images/images/4.jpg', description: 'Image 4'}
			    ];



		vm.mainImageUrl = vm.pictures[0];

		vm.setImage = function(imageUrl) {
	      vm.pictures[0] = imageUrl;
	    };

      vm.account = null;

      vm.account = Principal.identity().then(function(account) {
          console.log(account);
          vm.account = account;
          vm.isAuthenticated = Principal.isAuthenticated;
      });



        vm.checkIn = new Date();
        vm.checkOut = new Date();

        vm.openLightboxModal = function (index) {
            vm.Lightbox.openModal(vm.images, index);
          };

        vm.awesomeThings = [
        'HTML5',
        'AngularJS',
        'Karma',
        'Slick',
        'Bower',
        'Coffee'
      ]

      // vm.handleClick = function (msg) {
      //   $scope.$emit('childEmit', {message: msg});
      // };

      var onSaveSuccess = function (result) {
          //$scope.$emit('mozhotelsApp:instanceReviewUpdate', result);
          vm.isSaving = false;
          vm.reviews = InstanceReview.query();
      };

      var onSaveError = function () {
          vm.isSaving = false;
      };

      vm.save = function () {
          console.log("ENTROU...");
          vm.isSaving = true;
          InstanceReview.save(vm.instanceReview, onSaveSuccess, onSaveError);
      };

      vm.priceSliderCleanliness = {
        value: 2.5,
        options: {
            floor: 0,
            ceil: 5,
            step: 0.1,
            precision: 1
        }
    }

   vm.share = function(){
      vm.instanceReview.cleanliness=vm.priceSliderCleanliness.value;
  };

    vm.priceSliderRoomConfort = {
      value: 2.5,
      options: {
          floor: 0.0,
          ceil: 5.0,
          step: 0.1,
          precision: 1.0
      }
  }

  vm.priceSliderLocation = {
    value: 2.5,
    options: {
        floor: 0.0,
        ceil: 5.0,
        step: 0.1,
        precision: 1.0
    }
}

vm.priceSliderServiceStaff = {
  value: 2.5,
  options: {
      floor: 0.0,
      ceil: 5.0,
      step: 0.1,
      precision: 1.0
  }
}

vm.priceSliderSleepQuality = {
  value: 2.5,
  options: {
      floor: 0.0,
      ceil: 5.0,
      step: 0.1,
      precision: 1.0
  }
}

vm.priceSliderValuePrice = {
  value: 2.5,
  options: {
      floor: 0.0,
      ceil: 5.0,
      step: 0.1,
      precision: 1.0
  }
}

vm.avgEval = function(provinceId) {
    return alasql('SELECT VALUE AVG(price), instanceTur->id as instanceTur FROM ? where (instanceTur->id)= ?',[vm.instanceroomtypes,provinceId]);
};

vm.login = LoginService.open;
  vm.isAuthenticated = Principal.isAuthenticated;


}

})();

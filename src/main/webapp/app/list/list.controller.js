(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('ListController', ListController);

    ListController.$inject = ['$scope', 'Principal','InstanceTur', 'Picture', 'DataUtils', 'InstanceRoomType'];

    function ListController ($scope, Principal, InstanceTur, Picture, DataUtils, InstanceRoomType) {
        var vm = this;
        vm.instanceturs = InstanceTur.query();
        vm.pictures = Picture.query();
		vm.instanceroomtypes = InstanceRoomType.query();
		
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        vm.checkIn = new Date();
        vm.checkOut = new Date();

	 	vm.MyData = [{income:1000}, {income:2000},
	                    {income:1200}, {income:4500},
	                    {income:3300}, {income:200},
	                    {income:4500}, {income:2300}];
	

	    vm.avgPrice = function(provinceId) {
	        return alasql('SELECT VALUE AVG(price), instanceTur->id as instanceTur FROM ? where (instanceTur->id)= ?',[vm.instanceroomtypes,provinceId]);
	    };

    }
})();

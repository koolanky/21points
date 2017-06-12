(function() {
    'use strict';
    angular
        .module('21PointsApp')
        .factory('Bloodpressure', Bloodpressure);

    Bloodpressure.$inject = ['$resource', 'DateUtils'];

    function Bloodpressure ($resource, DateUtils) {
        var resourceUrl =  'api/bloodpressures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'last30Days':{method :'GET', isArray:false, url:'/api/bp-by-days/30'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();

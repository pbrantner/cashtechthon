(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('dashboardService', ['$http', DashboardService]);

    /**
     * Dashboard Data Service
     *
     * @returns {{loadStatistics: Function}}
     */
    function DashboardService($http) {
        return {
            loadStatistics : function() {
                return $http.get('/classifications');
            }
        };
    }

})();

(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('dashboardService', ['$q', '$http', DashboardService]);

    /**
     * Dashboard Data Service
     *
     * @returns {{loadStatistics: Function}}
     */
    function DashboardService($q, $http) {
        return {
            loadStatistics : function() {
                return $http.get('/classifications');
            }
        };
    }

})();

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
            loadStatistics : function(from, till) {
                return $http.get('/classifications/summary?from=' + from + '&till=' + till);
            }
        };
    }

})();

(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('dashboardService', ['$q', DashboardService]);

    /**
     * Dashboard Data Service
     *
     * @returns {{loadStatistics: Function}}
     */
    function DashboardService($q) {
        return {
            loadStatistics : function() {
                var statistics = { "name": "data" };
                //var statistics = $http.get('/classifications');
                return $q.when(statistics);
            }
        };
    }

})();

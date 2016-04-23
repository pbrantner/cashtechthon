(function(){

    angular
        .module('MDC')
        .controller('DashboardController', [
            'dashboardService', '$log', '$state',
            DashboardController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function DashboardController(dashboardService, $log, $state ) {
        var self = this;

        self.statistics        = { };

        dashboardService
            .loadStatistics()
            .then( function( statistics ) {
                self.statistics    = statistics;
            });

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(){
            $state.go('user');
        };
    }



})();

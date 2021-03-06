(function(){

    angular
        .module('MDC')
        .controller('DashboardController', [
            'dashboardService', 'commonService', '$log', '$state',
            'ReportHistoryService',
            DashboardController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function DashboardController(dashboardService, commonService, $log, $state, ReportHistoryService) {
        var self = this;

        self.statistics = {};
        self.common = commonService;
        self.reportHistory = ReportHistoryService.getHistory();

        dashboardService.loadStats().then(function (resp) {
            self.statistics = resp.data;
        },function (resp) {

        });

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(){
            $state.go('user');
        };

    }



})();

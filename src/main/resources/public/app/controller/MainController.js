(function(){

    angular
        .module('MDC')
        .controller('MainController', [
            'mainService', '$log', '$state',
            MainController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function MainController(mainService, $log, $state ) {
        var self = this;

        self.users        = [ ];

        // Load all registered users

        mainService
            .loadAllUsers()
            .then( function( users ) {
                self.users    = [].concat(users);
            });

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(){
            $state.go('user');
        };
    }



})();

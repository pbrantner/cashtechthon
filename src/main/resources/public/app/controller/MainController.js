(function(){

    angular
        .module('MDC')
        .controller('MainController', [
            'mainService', '$log',
            MainController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function MainController(mainService, $log ) {
        var self = this;

        self.users        = [ ];

        // Load all registered users

        mainService
            .loadAllUsers()
            .then( function( users ) {
                self.users    = [].concat(users);
            });
    }

})();

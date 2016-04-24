(function(){

    angular
        .module('MDC')
        .controller('CustomerController', [
            'customerService', 'commonService', '$stateParams', '$log',
            CustomerController
        ]);


    /**
     * Manages details for a specific customer
     */
    function CustomerController(customerService, commonService, $stateParams, $log ) {
        var self = this;

        self.customer        = { };
        self.customerId = $stateParams.customerId;

        var f = commonService.from.toISOString().slice(0,10);
        var t = commonService.till.toISOString().slice(0,10);

        self.tags = ['Amazon', 'PayPal', '...'];

        self.companies = [{
            name: "Amazon",
            icon: "./assets/amazon.png"
        },{
            name: "PayPal",
            icon: "./assets/paypal.png"
        }];

        self.connections = [];
        self.planned = [];

        customerService
            .get(self.customerId, f, t)
            .then( function( customers ) {
                var customer = customers.data.length > 0 ? customers.data[0] : {};
                console.log("customer " + self.customerId + "'s details loaded");
                customer.avatar = "http://www.gravatar.com/avatar/" + CryptoJS.MD5(customer.firstName + " "
                        + customer.lastName) + "?s=120&d=identicon";
                self.tags = customer.classifications || self.tags;
                self.customer    = customer;
            });

        customerService
            .getCompanies(self.customerId)
            .then(function(comps){
                self.companies = [].concat(comps.data.content || self.companies);
                //self.companies = comps.content || self.companies;
            });

    }

})();

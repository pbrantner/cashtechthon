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

        self.download = function() {
            var f = commonService.from.toISOString().slice(0,10);
            var t = commonService.till.toISOString().slice(0,10);

            customerService
                .getCsv(self.customerId, f, t)
                .then( function( data ) {
                    var d = data.data;
                    var blob = new Blob([d], {type: "text/csv;charset=utf-8"});
                    saveAs(blob, "data.csv");
                });
        };

        var f = commonService.from.toISOString().slice(0,10);
        var t = commonService.till.toISOString().slice(0,10);

        self.tags = ['Amazon', 'PayPal', '...'];

        customerService
            .get(self.customerId, f, t)
            .then( function( customer ) {
                $log.debug("customer " + self.customerId + "'s details loaded");
                customer.data.avatar = "http://www.gravatar.com/avatar/" + CryptoJS.MD5(customer.data.firstName + " "
                        + customer.data.lastName) + "?s=120&d=identicon";
                self.tags = customer.classifications || self.tags;
                self.customer    = customer.data;
            });

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
                self.customer    = customer.data;
            });

        customerService
            .getCompanies(self.customerId)
            .then(function(comps){
                self.companies = [].concat(comps.data.content || self.companies);
                //self.companies = comps.content || self.companies;
            });

        function toMilliseconds(minutes) {
            return minutes * 60 * 1000;
        }

        function drawChart() {

            var otherData = new google.visualization.DataTable();
            otherData.addColumn('string', 'Task ID');
            otherData.addColumn('string', 'Task Name');
            otherData.addColumn('string', 'Resource');
            otherData.addColumn('date', 'Start');
            otherData.addColumn('date', 'End');
            otherData.addColumn('number', 'Duration');
            otherData.addColumn('number', 'Percent Complete');
            otherData.addColumn('string', 'Dependencies');

            otherData.addRows([
                ['meAndMyBaby', 'meAndmyBaby GmbH', null, new Date(2016,1,10), new Date(2016,1,15), toMilliseconds(1), 100, null],
                ['kinderzimmer', 'Kinderzimmereinrichter E.U.', null, new Date(2016, 3, 1), new Date(2016, 3, 6), toMilliseconds(1), 100, null],
                ['child', 'Neugeborenes/Kleinkind', null, new Date(2016,3,15), new Date(2016,3,20), toMilliseconds(1), 100, 'meAndMyBaby,kinderzimmer'],
                ['bausparer', 'Bausparer', null, new Date(2016,3,24), new Date(2016,3,29), toMilliseconds(1), 100, 'child']
            ]);

            var options = {
                height: 275,
                gantt: {
                    defaultStartDateMillis: new Date(2016,4,23)
                }
            };

            var chart = new google.visualization.Gantt(document.getElementById('trendchart'));

            chart.draw(otherData, options);
        }

        drawChart();
    }

})();

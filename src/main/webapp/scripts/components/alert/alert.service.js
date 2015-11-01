(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('AlertService', AlertService);

  function AlertService($timeout, $sce, $translate) {
    var alertId = 0; // unique id for each alert. Starts from 0.
    var alerts = [];
    var timeout = 5000; // default timeout

    return {
      add: addAlert,
      clear: clear,
      closeAlert: closeAlert,
      closeAlertByIndex: closeAlertByIndex,
      error: error,
      factory: factory,
      get: get,
      info: info,
      success: success,
      warning: warning
    };

    ////////////////

    function clear() {
      alerts = [];
    }

    function get() {
      return alerts;
    }

    function success(msg, params) {
      this.add({
        type: "success",
        msg: msg,
        params: params,
        timeout: timeout
      });
    }

    function error(msg, params) {
      this.add({
        type: "danger",
        msg: msg,
        params: params,
        timeout: timeout
      });
    }

    function warning(msg, params) {
      this.add({
        type: "warning",
        msg: msg,
        params: params,
        timeout: timeout
      });
    }

    function info(msg, params) {
      this.add({
        type: "info",
        msg: msg,
        params: params,
        timeout: timeout
      });
    }

    function factory(alertOptions) {
      return alerts.push({
        type: alertOptions.type,
        msg: $sce.trustAsHtml(alertOptions.msg),
        id: alertOptions.alertId,
        timeout: alertOptions.timeout,
        close: function () {
          return exports.closeAlert(this.id);
        }
      });
    }

    function addAlert(alertOptions) {
      alertOptions.alertId = alertId++;
      alertOptions.msg = $translate.instant(alertOptions.msg, alertOptions.params);
      var that = this;
      this.factory(alertOptions);
      if (alertOptions.timeout && alertOptions.timeout > 0) {
        $timeout(function () {
          that.closeAlert(alertOptions.alertId);
        }, alertOptions.timeout);
      }
    }

    function closeAlert(id) {
      return this.closeAlertByIndex(alerts.map(function (e) {
        return e.id;
      }).indexOf(id));
    }

    function closeAlertByIndex(index) {
      return alerts.splice(index, 1);
    }
  }

})();

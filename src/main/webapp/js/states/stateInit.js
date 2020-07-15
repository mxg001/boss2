function initStates(stateProvider){
	console.log("initStates--start");
	try { initSystem(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initSystem-Finish"); }
	try { initSystemPart2(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initSystemPart2-Finish"); }
	try { initAllAgent(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initAllAgent-Finish"); }
	try { initCreditMgr(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initCreditMgr-Finish"); }
	try { initExchange(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initExchange-Finish"); }
	try { initExchangeActivate(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initExchangeActivate-Finish"); }
	try { initSuperBank(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initSuperBank-Finish"); }
	try { initCjt(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initCjt-Finish"); }
	try { initCreditRepay(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initCreditRepay-Finish"); }
	console.log("initStates--end");
}
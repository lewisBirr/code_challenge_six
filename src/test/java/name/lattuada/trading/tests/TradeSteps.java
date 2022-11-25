package name.lattuada.trading.tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TradeSteps {

    private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
    private final RestUtility restUtility;
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private final Map<String, OrderDTO> orderMap;
    public OrderDTO buyOrder;
    public OrderDTO sellOrder;

    TradeSteps() {

        restUtility = new RestUtility();
        securityMap = new HashMap<>();
        userMap = new HashMap<>();
        orderMap = new HashMap<>();
    }



    // TODO implement: Given for "one security {string} and two users {string} and {string} exist"
    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsers(String securityName, String userName1, String userName2) {
        createSecurity(securityName);
        createUser(userName1);
        createUser(userName2);

    }

    @When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
    @And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
    public void userPutAnOrder(String userName, String orderType, String securityName, Double price, Long quantity) {
        logger.trace("Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
                userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
        assertTrue(String.format("Unknown user \"%s\"", userName),
                userMap.containsKey(userName));
        assertTrue(String.format("Unknown security \"%s\"", securityName),
                securityMap.containsKey(securityName));
        createOrder(userName,
                EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)),
                securityName,
                price,
                quantity);

    }


    @Then("a trade occurs with the price of {double} and quantity of {long}")
    public void aTradeOccursWithThePriceOfAndQuantityOf(Double price, Long quantity) {
        logger.trace("Got price = \"{}\"; quantity = \"{}\"",
                price, quantity);
        TradeDTO trade = restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class);
        assertEquals("Price not expected", trade.getPrice(), price);
        assertEquals("Quantity not expected", trade.getQuantity(), quantity);
    }

    @Then("no trades occur")
    public void noTradesOccur() {
        assertThatThrownBy(() -> restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class)).isInstanceOf(HttpClientErrorException.NotFound.class);
    }



    private void createUser(String userName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userName);
        userDTO.setPassword(RandomStringUtils.randomAlphanumeric(64));
        UserDTO userReturned = restUtility.post("api/users",
                userDTO,
                UserDTO.class);
        userMap.put(userName, userReturned);
        logger.info("User created: {}", userReturned);
    }

    private void createSecurity(String securityName) {
        SecurityDTO securityDTO = new SecurityDTO();
        securityDTO.setName(securityName);
        SecurityDTO securityReturned = restUtility.post("api/securities",
                securityDTO,
                SecurityDTO.class);
        securityMap.put(securityName, securityReturned);
        logger.info("Security created: {}", securityReturned);
    }

    private void createOrder(String userName,
                             EOrderType orderType,
                             String securityName,
                             Double price,
                             Long quantity) {


        // TODO: implement create oder function
        if (orderType.name().equals("BUY")) {

            OrderDTO buyOrder1 = new OrderDTO();
            buyOrder1.setUserId(userMap.get(userName).getId());
            buyOrder1.setType(orderType);
            buyOrder1.setSecurityId(securityMap.get(securityName).getId());
            buyOrder1.setPrice(price);
            buyOrder1.setQuantity(quantity);
            buyOrder = restUtility.post("/api/orders",buyOrder1,OrderDTO.class);
            orderMap.put(userName,buyOrder);
            logger.info("Order created: {}",buyOrder);

            /*
            buyOrder = new OrderDTO();
            buyOrder.setUserId(userMap.get(userName).getId());
            buyOrder.setType(orderType);
            buyOrder.setSecurityId(securityMap.get(securityName).getId());
            buyOrder.setPrice(price);
            buyOrder.setQuantity(quantity);
            OrderDTO buyOrderReturned = restUtility.post("/api/orders",buyOrder,OrderDTO.class);
            orderMap.put(userName,buyOrderReturned);
            //TradeDTO trade = restUtility.post("api/trades/orderBuyId/", buyOrder ,TradeDTO.class);
            logger.info("Order created: {}",buyOrderReturned ); */


        } else if (orderType.name().equals("SELL")) {

            OrderDTO sellOrder1 = new OrderDTO();
            sellOrder1.setUserId(userMap.get(userName).getId());
            sellOrder1.setType(orderType);
            sellOrder1.setSecurityId(securityMap.get(securityName).getId());
            sellOrder1.setPrice(price);
            sellOrder1.setQuantity(quantity);
            sellOrder = restUtility.post("/api/orders",sellOrder1,OrderDTO.class);
            orderMap.put(userName,sellOrder);
            // TradeDTO trade = restUtility.post("api/trades/orderBuyId/", sellOrder ,TradeDTO.class);
            logger.info("Order created: {}",sellOrder);

            /*
            sellOrder = new OrderDTO();
            sellOrder.setUserId(userMap.get(userName).getId());
            sellOrder.setType(orderType);
            sellOrder.setSecurityId(securityMap.get(securityName).getId());
            sellOrder.setPrice(price);
            sellOrder.setQuantity(quantity);
            OrderDTO sellOrderReturned = restUtility.post("/api/orders",sellOrder,OrderDTO.class);
            orderMap.put(userName,sellOrderReturned);
            // TradeDTO trade = restUtility.post("api/trades/orderBuyId/", sellOrder ,TradeDTO.class);
            logger.info("Order created: {}",sellOrderReturned ); */

        }
    }
}

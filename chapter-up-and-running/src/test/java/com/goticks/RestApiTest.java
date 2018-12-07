package com.goticks;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import akka.http.javadsl.model.*;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.junit.Before;
import org.junit.Test;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;

import java.time.Duration;


public class RestApiTest extends JUnitRouteTest {
  private TestRoute appRoute;


  @Before
  public void initClass() {
    final ActorSystem system = ActorSystem.create("go-ticks");
    RestApi server = new RestApi(system, Duration.ofSeconds(5));
    appRoute = testRoute(server.createRoute());
  }

  @Test
  public void testNoEvents() {
    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[]}");
  }

  @Test
  public void testCreateEvent() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");
  }

  @Test
  public void testGetEvents() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[{\"name\":\"RHCP\",\"tickets\":3}]}");
  }

  @Test
  public void testBuy() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP/tickets")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 2}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"entries\":[{\"id\":1},{\"id\":2}],\"event\":\"RHCP\"}");
  }

  @Test
  public void testBuyAndGetEvents() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP/tickets")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 2}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"entries\":[{\"id\":1},{\"id\":2}],\"event\":\"RHCP\"}");

    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[{\"name\":\"RHCP\",\"tickets\":1}]}");
  }

  @Test
  public void testBuyNotEnough() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP/tickets")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 4}"))
        .assertStatusCode(StatusCodes.NOT_FOUND)
        .assertMediaType("text/plain")
        .assertEntity("The requested resource could not be found but may be available again in the future.");

    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[{\"name\":\"RHCP\",\"tickets\":3}]}");
  }

  @Test
  public void testBuyAll() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP/tickets")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"entries\":[{\"id\":1},{\"id\":2},{\"id\":3}],\"event\":\"RHCP\"}");

    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[{\"name\":\"RHCP\",\"tickets\":0}]}");
  }

  @Test
  public void testBuyNotFound() {
    appRoute.run(HttpRequest.POST("/events/RHCP/tickets")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 2}"))
        .assertStatusCode(StatusCodes.NOT_FOUND)
        .assertMediaType("text/plain")
        .assertEntity("The requested resource could not be found but may be available again in the future.");
  }

  @Test
  public void testCancel() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.DELETE("/events/RHCP"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");
  }

  @Test
  public void testCancelAndGetEvents() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.DELETE("/events/RHCP"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.GET("/events"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"events\":[]}");
  }

  @Test
  public void testCancelNotFound() {
    appRoute.run(HttpRequest.DELETE("/events/RHCP"))
        .assertStatusCode(StatusCodes.NOT_FOUND)
        .assertMediaType("text/plain")
        .assertEntity("The requested resource could not be found but may be available again in the future.");
  }

  @Test
  public void testAlreadyExists() {
    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.BAD_REQUEST)
        .assertMediaType("application/json")
        .assertEntity("{\"message\":\"RHCP exists already.\"}");
  }

  @Test
  public void testGetAEvent() {
    appRoute.run(HttpRequest.POST("/events/RHCP1/")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 3}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP1\",\"tickets\":3}");

    appRoute.run(HttpRequest.POST("/events/RHCP2/")
        .withEntity(MediaTypes.APPLICATION_JSON.toContentType(),
            "{\"tickets\": 2}"))
        .assertStatusCode(StatusCodes.CREATED)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP2\",\"tickets\":2}");

    appRoute.run(HttpRequest.GET("/events/RHCP1/"))
        .assertStatusCode(StatusCodes.OK)
        .assertMediaType("application/json")
        .assertEntity("{\"name\":\"RHCP1\",\"tickets\":3}");
  }

  @Test
  public void testGetAEventNotFound() {
    appRoute.run(HttpRequest.GET("/events/RHCP1/"))
        .assertStatusCode(StatusCodes.NOT_FOUND)
        .assertMediaType("text/plain")
        .assertEntity("The requested resource could not be found but may be available again in the future.");
  }
}

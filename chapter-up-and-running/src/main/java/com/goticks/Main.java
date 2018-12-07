package com.goticks;


import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;


public class Main {
  public static void main(String[] args) throws IOException {

    // 設定からホスト名とポートを取得
    final Config config = ConfigFactory.load();
    final String host = config.getString("http.host");
    final int port = config.getInt("http.port");

    final ActorSystem system = ActorSystem.create("go-ticks");
    final LoggingAdapter log = Logging.getLogger(system, Main.class);
    log.info("start actor system: {}", system.name());

    final Http http = Http.get(system);
    final ActorMaterializer materializer = ActorMaterializer.create(system);

    RestApi app = new RestApi(system, Duration.ofSeconds(5));

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
        ConnectHttp.toHost(host, port), materializer); // HTTPサーバーの起動

    log.info("Server online at http://{}:{}", host, port);
    log.info("Press RETURN to stop...");

    System.in.read();

    log.info("presses return...");

    binding
        .thenCompose(ServerBinding::unbind)
        .thenAccept(unbound -> system.terminate());


  }
}

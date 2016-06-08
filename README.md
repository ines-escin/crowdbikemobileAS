# bikecidadaoAS [![Build Status](https://travis-ci.org/jonaslins/crowdbikemobileAS.svg?branch=master)](https://travis-ci.org/jonaslins/crowdbikemobileAS)

O BikeCidadao é uma aplicação móvel de crowdsensing para a navegação de bicicleta e compartilhamento de informação para outros ciclistas. O seu recurso de crowdsensing permite compartilhar pontos de perigo nas ruas da cidade, informar as condições meteorológicas e enviar em tempo real alertas de voz de pontos perigosos mais próximos. Ele foi classificado em terceiro lugar no SCAH - FIWARE Online Competition, prêmio recebido no Smart City Expo World Congress, realizado em Barcelona, em 11/2015. [Aqui](https://vimeo.com/145393719) tem o vídeo explicando o conceito. O pitch feito no congresso está aqui: [Pitch Smart City Expo World Congress](http://bambuser.com/v/5932333).  Ele foi classificado em terceiro lugar no [SCAH - FIWARE Online Competition](http://fiware.smartcityapphack.com), prêmio recebido no [Smart City Expo World Congress](http://www.smartcityexpo.com/), realizado em Barcelona, em 11/2015. E o aplicativo esta na loja: [Bike Cidadão Play Store](https://play.google.com/store/apps/details?id=br.ufpe.cin.contexto.bikecidadao).

-------------------------------------------------------------------------------------------------------------------

## Documentação de features e cenários

Para contribuir com novas ou existentes features/cenários é preciso documentá-los nos arquivos .feature, localizados em [app/src/androidTest/assets/features](app/src/androidTest/assets/features). Lembre-se de seguir as [regras de contribuição](#regras-para-contribuir) detalhadas mais abaixo.

Os arquivos **.feature** são escritos na linguagem chamada Gherkin e registram tanto a documentação do projeto quanto auxiliam no desenvolvimento, evolução e implementação de testes usando o [Cucumber](https://cucumber.io/). O link a seguir explica resumidamente como é o formato e os conceitos que estruturam o Cucumber: https://cucumber.io/docs/reference


## Regras para contribuir

- Gerenciar atividades de desenvolvimento e teste através do PivotalTracker (https://www.pivotaltracker.com/n/projects/1377146);
- Integrar PivotalTracker e GitHub a fim de ser possível identificar os commits associados à uma tarefa;
- Padrão para mensagem de commit: <b>[#ID] message</b> e <b>[completed #ID] message</b>, sendo <b>ID</b> o identificador da tarefa no PivotalTracker;
- Tarefa de funcionalidade = User story do tipo feature do PivotalTracker;
- Tarefa de teste = User story do tipo chore do PivotalTracker;
- Padrão para título de tarefa de teste no PivotalTracker: <b>Test #ID_FEATURE</b>;
- Caso as tarefas de funcionalidade e teste sejam realizadas pela mesma pessoa, pode ser definida uma user story única no PivotalTracker para ambas;
- Commits que não estiverem relacionados à uma funcionalidade específica não devem ter ID;
- Um commit pode estar relacionado à mais de uma tarefa, o que significa que sua mensagem pode conter mais de um ID. Formato: <b>[#ID_1 #ID_2]</b>, sendo <b>ID_1</b> o identificador da tarefa 1 e <b>ID_2</b> o identificador da tarefa 2.

Em caso de dúvidas, entrar em contato com <b>Thaís Burity</b> (https://github.com/thaisabr).

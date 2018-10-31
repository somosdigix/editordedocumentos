def pathDoRepositorio = 'https://solucoesdigix.visualstudio.com/Projetos/_git/EditorDeDocumento'
def nomeDaBranch = 'master'
def idDaCredencialDoTFS = '23982c6f-8c4a-48a5-8a37-58e145846057'
def pomRaizDoProjeto = 'pom.xml'
def instalacaoDoMaven = 'Maven3.2.2'
def quantidadeDeDiasDeArmazenamentoDasBuilds = -1
def quantidadeDeBuildsArmazenadas = 20
def membrosDoTime = "rafaelgoncalves, diegopaniago, rsalcir, jeandonato, victorjussiani"
def nomeDoDominioDoSlack = 'digithobrasil'
def servidorQueIraExecutarOsJobsDeDev = 'slave03'
def nomeDoCanalDoSlackDoTime = '#bichodagoiaba'
def tokenDoSlack = 'h8GM43iV16fM9PEuRPBNISJc'
def notificarNoSlack = { projeto ->
    projeto.with {
        publishers {
            slackNotifier  {
                teamDomain(nomeDoDominioDoSlack)
                authToken(tokenDoSlack)
                room(nomeDoCanalDoSlackDoTime)
                notifyAborted(true)
                notifyFailure(true)
                notifyBackToNormal(true)
                includeCustomMessage(true)
                customMessage('Galeris deu ruim na build!')
                commitInfoChoice('AUTHORS_AND_TITLES')
            }
        }
    }
}

def pathDoWorkspaceCompartilhado = "\$JENKINS_HOME/workspace/EditorDeDocumento-SHARED"

deliveryPipelineView("EditorDeDocumento-Pipeline") {
    pipelines {
        component "EditorDeDocumento-Pipeline", "EditorDeDocumento-CI"
    }
    allowPipelineStart()
}

job("EditorDeDocumento-CI") {
    customWorkspace pathDoWorkspaceCompartilhado
    deliveryPipelineConfiguration 'Commit-Stage', 'Testes'
    blockOnDownstreamProjects()
    label servidorQueIraExecutarOsJobsDeDev
    wrappers {
        preBuildCleanup()
    }
    scm {
        git {
            branch nomeDaBranch
            remote {
                url pathDoRepositorio
                credentials idDaCredencialDoTFS
            }
        }
    }
    steps {
        maven {
            goals "clean install"
            rootPOM pomRaizDoProjeto
            mavenInstallation instalacaoDoMaven
        }
    }
    publishers {
        publishBuild {
            discardOldBuilds(quantidadeDeDiasDeArmazenamentoDasBuilds, quantidadeDeBuildsArmazenadas)
        }
        downstream "EditorDeDocumento-Qualidade"
    }
}

job("EditorDeDocumento-Qualidade") {
    customWorkspace pathDoWorkspaceCompartilhado
    deliveryPipelineConfiguration 'Commit-Stage', 'Qualidade'
    blockOnDownstreamProjects()
    label servidorQueIraExecutarOsJobsDeDev
    notificarNoSlack it
    steps {
        maven {
            goals 'cobertura:cobertura sonar:sonar'
            rootPOM pomRaizDoProjeto
            mavenInstallation instalacaoDoMaven
        }
    }
    properties {
        promotions {
            promotion {
                name('Deploy no nexus da Digix')
                icon('star-gold')
                conditions {
                    manual(membrosDoTime)
                }
                actions {
                    downstreamParameterized {
                        trigger "EditorDeDocumento-Deploy"
                    }
                }
            }
        }
    }
    publishers {
        archiveArtifacts {
            pattern("pom.xml")
            onlyIfSuccessful()
        }
        publishBuild {
            logRotator(quantidadeDeDiasDeArmazenamentoDasBuilds, quantidadeDeBuildsArmazenadas)
        }
    }
}

job("EditorDeDocumento-Deploy") {
    deliveryPipelineConfiguration 'Deploy', "Deploy-Digix"
    blockOnDownstreamProjects()
    label servidorQueIraExecutarOsJobsDeDev
    notificarNoSlack it
    steps {
        copyArtifacts("EditorDeDocumento-Qualidade") {
            buildSelector {
                upstreamBuild {
                    allowUpstreamDependencies false
                }
            }
        }
        maven {
            goals "deploy"
            rootPOM pomRaizDoProjeto
            mavenInstallation instalacaoDoMaven
        }
    }
    publishers {
        publishBuild {
            discardOldBuilds(quantidadeDeDiasDeArmazenamentoDasBuilds, quantidadeDeBuildsArmazenadas)
        }
    }
}

queue("EditorDeDocumento-CI")

<?php

// Array para cria��o da mensagem JSON
$response = array();

if (isset($_POST['email']) && isset($_POST['course'])){
    
    $name = $_POST['email'];
    $course = $_POST['course'];

    // Classe assitente para conex�o com o banco de dados no servidor
    require_once __DIR__ . '/db_connect.php';

    $db = new DB_CONNECT();

    // Inser��o dos dados recebidos no cadastro pelo Android
    $result = mysql_query("INSERT INTO enrollment_table(email, course) VALUES('$name', '$course')");

    if ($result) {
        // Sucesso ao inserir dados
        $response["success"] = 1;
        $response["message"] = "Sucesso na cria�ao da mensagem.";

        echo json_encode($response);
    } else {
        // Erro ao inserir dados
        $response["success"] = 0;
        $response["message"] = "Erro.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // Erro. Algum campo n�o foi enviado
    $response["success"] = 0;
    $response["message"] = "Campo obrigatorio nao fornecido.";

    echo json_encode($response);
}
?>
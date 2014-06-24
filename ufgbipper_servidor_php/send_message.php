<?php
// Array JSON para armazenar as mensagens
$response = array();
 
// Classe auxiliar
require_once __DIR__ . '/db_connect.php';
 
// Conexao com o banco
$db = new DB_CONNECT();
 
// Busca por remetente, data e conteudo das mensagens
$result = mysql_query("SELECT sender, date, message FROM message_table WHERE course='inf_c01'") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    
	// Existem mensagens enviadas
    $response["messages"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // Nos do array JSON
        $message = array();
        $message["sender"] = $row["sender"];
        $message["date"] = $row["date"];
        $message["message"] = $row["message"];
 
        array_push($response["messages"], $message);
    }
    // 
    $response["success"] = 1;
 
    // Exibe resposta na tela
    echo json_encode($response);
} else {
    // Sem mensagens para serem enviadas
    $response["success"] = 0;
    $response["message"] = "Sem Mensagens";
 
    // Exibe reposta na tela
    echo json_encode($response);
}
?>
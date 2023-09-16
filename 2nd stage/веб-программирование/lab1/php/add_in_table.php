<tr class="table-header">
    <th scope="col">X</th>
    <th scope="col">Y</th>
    <th scope="col">R</th>
    <th scope="col">Текущее время</th>
    <th scope="col">Время исполнения</th>
    <th scope="col">Результат попадания</th>
</tr>

<?php if (!key_exists("batman0v", $_SESSION)) $_SESSION["batman0v"] = array();
foreach ($_SESSION["batman0v"] as $value){ ?>
    <tr class="table-row">
        <td><?php echo $value[0] ?></td>
        <td><?php echo $value[1] ?></td>
        <td><?php echo $value[2] ?></td>
        <td><?php echo $value[3] ?></td>
        <td><?php echo $value[4] ?> ms</td>
        <?php echo $value[5] ? "<td style='color: green'>Попал</td>" : "<td style='color: violet'>Промазал</td>"; ?>
    </tr>
<?php } ?>
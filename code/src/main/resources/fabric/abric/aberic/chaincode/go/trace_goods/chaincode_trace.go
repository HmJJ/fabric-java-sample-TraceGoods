package main

import (
	"fmt"
	"strconv"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type TraceChaincode struct {
}

/* 定义商品实体 */
type Goods struct {
	Id string            //商品id
	Name string          //商品名称
	Price string         //商品价格
	RegisterDate string  //生产日期
}

/* 定义物流实体 */
type Logistic struct {
	Id string            //物流id
	GoodsId string       //商品id
	CityName string      //城市名称
}

/* 商品列表key */
var goodsListKey = "sheep_goods_list"

/* 合约初始化入口 */
func (t *TraceChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("trace_goods Init")
	
	return shim.Success(nil)
}

/* 合约方法执行入口 */
func (t *TraceChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("the_dragon Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function == "invoke" {
		// Make payment of X units from A to B
		return t.invoke(stub, args)
	} else if function == "query" {
		// the old "Query" is now implemtned in invoke
		return t.query(stub, args)
	} else if function == "queryAllGoods" {
		// the old "Query" is now implemtned in invoke
		return t.queryAllGoods(stub, args)
	} else if function == "queryLogisticByGoodsId" {
		// the old "Query" is now implemtned in invoke
		return t.queryLogisticByGoodsId(stub, args)
	} else if function == "addGoods" {
		return  t.addGoods(stub, args)
	} else if function == "modifyGoods" {
		return  t.modifyGoods(stub, args)
	} else if function == "addLogistic" {
		return  t.addLogistic(stub, args)
	} else if function == "modifyLogistic" {
		return t.modifyLogistic(stub, args)
	} else if function == "deleteGoods" {
		// Deletes an entity from its state
		return t.deleteGoods(stub, args)
	} else if function == "deleteLogistic" {
		// Deletes an entity from its state
		return t.deleteLogistic(stub, args)
	}

	return shim.Error("Invalid invoke function name."+ function + "Expecting \"invoke\" \"delete\" \"query\" \"queryAllGoods\" \"queryLogisticByGoodsId\" \"addGoods\" \"modifyGoods\" \"addLogistic\" \"modifyLogistic\" \"deleteGoods\" \"deleteLogistic\" ")
}

/* 添加商品信息 */
func (t *TraceChaincode)addGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, registerDate string
	var err error
	isExist := true

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	name = args[1]
	price = args[2]
	registerDate = args[3]

	fmt.Printf("id = %s,goodsName=%s, price = %s, registerDate = %s\n", id, name, price, registerDate)


	goodsInfo, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if goodsInfo != nil {
		isExist = false
	}

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, RegisterDate:registerDate}
	goodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, goodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [3]string
	data[0] = "添加商品成功"
	data[1] = string(goodsbytes)

	if isExist {
		var goodsIds []string
		goodsIdsBytes, err := stub.GetState(goodsListKey)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsListKey + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsbytes != nil {
			err = json.Unmarshal(goodsIdsBytes, &goodsIds)
		}

		goodsIds = append(goodsIds, id)

		newgoodsIdsBytes,_ := json.Marshal(goodsIds)
		err = stub.PutState(goodsListKey, newgoodsIdsBytes)
		if err != nil {
			data[2] = err.Error()
		} else {
			data[2] = strconv.FormatBool(isExist)
		}
	} else {
		data[2] = strconv.FormatBool(isExist)
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 修改商品信息 */
func (t *TraceChaincode)modifyGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, registerDate string
	var err error

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	name = args[1]
	price = args[2]
	registerDate = args[4]

	fmt.Printf("id = %s,goodsName=%s, price = %s, registerDate = %s\n", id, name, price, registerDate)

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, RegisterDate:registerDate}
	goodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, goodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "修改商品成功"
	data[1] = string(goodsbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 添加物流信息 */
func (t *TraceChaincode)addLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName string
	var err error

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	goodsId = args[1]
	cityName = args[2]

	fmt.Printf("id = %s,goodsId=%s, price = %s, registerDate = %s\n", id, goodsId, cityName)

	// Write the state to the ledger
	logistic := Logistic{Id:id, GoodsId:goodsId, CityName:cityName}

	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(key, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "添加物流信息成功"
	data[1] = string(logisticbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 修改物流信息 */
func (t *TraceChaincode)modifyLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName string
	var err error

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	goodsId = args[1]
	cityName = args[2]

	fmt.Printf("id = %s,goodsId=%s, price = %s, registerDate = %s\n", id, goodsId, cityName)

	// Write the state to the ledger
	logistic := Logistic{Id:id, GoodsId:goodsId, CityName:cityName}
	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(id, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "修改物流信息成功"
	data[1] = string(logisticbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* invoke方法 */
func (t *TraceChaincode) invoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	
	return shim.Success(nil)
}

/* 查看 */
func (t *TraceChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var key string

	key = args[0]

	keybytes, err := stub.GetState(key)
	if err != nil {
		shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "查询成功"
	data[1] = string(keybytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 查看所有商品信息 */
func (t *TraceChaincode) queryAllGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	goodsIdsBytes, err := stub.GetState(goodsListKey)
	if err != nil {
		shim.Error(err.Error())
	}
	
	var goodsIds []string

	err = json.Unmarshal(goodsIdsBytes, &goodsIds)

	// goodsIds = strings.Split(goodsIdStr, ",")

	var goodsBytes []byte
	for index := range goodsIds {

		// var logInfo [1]string

		id := goodsIds[index]

		// logInfo[0] = id
		// logbytes,_ := json.Marshal(logInfo)

		// return shim.Success(logbytes)

		goodsIdbytes, err := stub.GetState(id)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsIdbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + id + "\"}"
			return shim.Error(jsonResp)
		}

		goodsBytes = append(goodsBytes, goodsIdbytes...)
	}

	var data [2]string
	data[0] = "查询成功"
	data[1] = string(goodsBytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 根据商品id查看商品的物流信息 */
func (t *TraceChaincode) queryLogisticByGoodsId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id string
	var data [2]string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]

	logisticsMap := []map[string]Logistic{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Goods~Logistic:", []string{id})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item, _ := resultIterator.Next()
		fmt.Printf("key=%s\n", item.Key)
		logisticJsonBytes, err := stub.GetState(item.Key)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		logistic := Logistic{}
	   	err  = json.Unmarshal(logisticJsonBytes, &logistic)
		if err != nil {
   			return shim.Error(err.Error())
   		}


		l := map[string]Logistic{"operation":logistic}
	    logisticsMap = append(logisticsMap, l)
	}
	logisticJson, err := json.Marshal(logisticsMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}

	data[0] = "查询成功"
	data[1] = string(logisticJson)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 删除商品信息 */
func (t *TraceChaincode) deleteGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	var id string
	var err error

	id = args[0]
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}

	// Delete the key from the state in ledger
	err = stub.DelState(id)
	if err != nil {
		return shim.Error("Failed to delete state")
	}

	return shim.Success(nil)
}


/* 删除物流信息 */
func (t *TraceChaincode) deleteLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	var id string
	var err error

	id = args[0]
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}

	// Delete the key from the state in ledger
	err = stub.DelState(id)
	if err != nil {
		return shim.Error("Failed to delete state")
	}

	return shim.Success(nil)
}

/* 合约入口 */
func main() {
	err := shim.Start(new(TraceChaincode))
	if err != nil {
		fmt.Printf("Error starting Trace chaincode: %s", err)
	}
}

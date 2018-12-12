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
	Id string
	Name string
	Price string
	RegisterDate string
}

/* 定义物流实体 */
type Logistic struct {
	Id string
	GoodsId string
	CityName string
}

/* 商品对应的所有物流信息 */
var goods_logistics = map[string][]string{}
/* 所有商品 */
var goodsIds []string
/* 所有物流 */
//var logistics = map[int]string{}

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
	} else if function == "queryAllGoods" {
		// the old "Query" is now implemtned in invoke
		return t.queryAllGoods(stub, args)
	} else if function == "queryGoodsById" {
		// the old "Query" is now implemtned in invoke
		return t.queryGoodsById(stub, args)
	} else if function == "queryGoodsByPage" {
		// the old "Query" is now implemtned in invoke
		return t.queryGoodsByPage(stub, args)
	} else if function == "queryAllLogistic" {
		// the old "Query" is now implemtned in invoke
		return t.queryAllLogistic(stub, args)
	} else if function == "queryLogisticByPage" {
		// the old "Query" is now implemtned in invoke
		return t.queryLogisticByPage(stub, args)
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

	return shim.Error("Invalid invoke function name."+ function + "Expecting \"invoke\" \"delete\" \"query\" \"addGoods\" \"modifyGoods\" \"addLogistic\" \"modifyLogistic\" \"deleteGoods\" \"deleteLogistic\" ")
}

/* 添加商品信息 */
func (t *TraceChaincode)addGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
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
	registerDate = args[3]

	fmt.Printf("id = %s,goodsName=%s, price = %s, registerDate = %s\n", id, name, price, registerDate)

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, RegisterDate:registerDate}
	goodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, goodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	goodsIds = append(goodsIds, id)
	var logisticIds []string
	goods_logistics[id] = logisticIds

	var data [2]string
	data[0] = "添加商品成功"
	data[1] = string(goodsbytes)
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
	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(id, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	//logistics[len(logistics)] = id
	var logisticIds []string
	logisticIds = goods_logistics[goodsId]
	logisticIds = append(logisticIds, id)
	goods_logistics[id] = logisticIds

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

/* 查看所有商品信息 */
func (t *TraceChaincode) queryAllGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var data [2]string
	var goodsInfos []byte

	for i := 0; i < len(goodsIds) - 1; i++ {
		// Get the state from the ledger
		goodsbytes, err := stub.GetState(goodsIds[i])
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + goodsIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		goodsInfos = append(goodsInfos, goodsbytes...)
	}

	

	data[0] = "查询成功"
	data[1] = string(goodsInfos)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 根据商品id查看商品信息 */
func (t *TraceChaincode) queryGoodsById(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id string
	var data [2]string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]

	// Get the state from the ledger
	goodsbytes, err := stub.GetState(id)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
		return shim.Error(jsonResp)
	}

	if goodsbytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + id + "\"}"
		return shim.Error(jsonResp)
	}

	data[0] = "查询成功"
	data[1] = string(goodsbytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 分页查看商品信息 */
func (t *TraceChaincode) queryGoodsByPage(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id string
	var pageNum, pageSize, start, end int
	var goodsInfos []byte
	var data [2]string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	pageNum, err = strconv.Atoi(args[0])
	if (err != nil) {
		return shim.Error("string changeTo int error")
	}
	pageSize, err = strconv.Atoi(args[1])
	if (err != nil) {
		return shim.Error("string changeTo int error")
	}
	start = (pageNum-1) * pageSize
	end = pageNum * pageSize - 1
	if (end > len(goodsIds)) {
		end = len(goodsIds) - 1
	}

	for i := start; i < end; i++ {
		id = goodsIds[i]

		// Get the state from the ledger
		goodsbytes, err := stub.GetState(id)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + id + "\"}"
			return shim.Error(jsonResp)
		}

		goodsInfos = append(goodsInfos, goodsbytes...)
	}

	

	data[0] = "查询成功"
	data[1] = string(goodsInfos)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 查看某商品的所有物流信息 */
func (t *TraceChaincode) queryAllLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var goodsId string
	var data [2]string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	goodsId = args[0]

	var logisticInfos []byte
	logisticIds := goods_logistics[goodsId]

	for i := 0; i < len(logisticIds) - 1; i++ {
		// Get the state from the ledger
		logisticbytes, err := stub.GetState(logisticIds[i])
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + logisticIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		if logisticbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + logisticIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		logisticInfos = append(logisticInfos, logisticbytes...)
	}

	

	data[0] = "查询成功"
	data[1] = string(logisticInfos)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 分页查看某商品的物流信息 */
func (t *TraceChaincode) queryLogisticByPage(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var goodsId string
	var pageNum, pageSize, start, end int
	var logisticInfos []byte
	var data [2]string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	goodsId = args[0]
	pageNum, err = strconv.Atoi(args[1])
	if (err != nil) {
		return shim.Error("string changeTo int error")
	}
	pageSize, err = strconv.Atoi(args[2])
	if (err != nil) {
		return shim.Error("string changeTo int error")
	}
	start = (pageNum-1) * pageSize
	end = pageNum * pageSize - 1
	if (end > len(goodsIds)) {
		end = len(goodsIds) - 1
	}

	var logisticIds = goods_logistics[goodsId]

	for i := start; i < end; i++ {
		// Get the state from the ledger
		logisticbytes, err := stub.GetState(logisticIds[i])
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + logisticIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		if logisticbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + logisticIds[i] + "\"}"
			return shim.Error(jsonResp)
		}

		logisticInfos = append(logisticInfos, logisticbytes...)
	}

	data[0] = "查询成功"
	data[1] = string(logisticInfos)
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

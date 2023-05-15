// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.19;

contract SupplyChainContract {
    struct ProductDates{
        uint64 plantingDate;
        uint64 harvestDate;
        uint64 processingDate;
        uint64 packagingDate;
        uint64 shipmentDate;
    }
    struct PaymentInfo {
        string paymentStatus;
        uint256 paymentDate;
        uint256 paymentAmount;
        uint256 paymentId;
    }
    struct productData {
        string farmLocation;
        string processingFacility;
        string packagingFacility;
        string shipmentLocation;
        string deliveryLocation;
        uint256 deliveryDate;
        PaymentInfo paymentInfo;
        ProductDates dates;
        address consumerId;
        address owner;
    }


    mapping(uint256 => productData) public products;
    uint256[] authorizedAddresses;
    address public owner;

    function addProduct(uint256 productId, string memory farmLocation, uint64 plantingDate) public {
        require(plantingDate <= block.timestamp, "Planting date cannot be in the future");
        productData storage product = products[productId];
        product.farmLocation = farmLocation;
        product.dates.plantingDate = plantingDate;
        product.owner = msg.sender;
    }

    function updateHarvestDate(uint256 productId, uint64 harvestDate) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        require(harvestDate > product.dates.harvestDate, "Planting date must be before harvest date");
        product.dates.harvestDate = harvestDate;
    }

    function updateProcessingFacility(uint256 productId, string memory processingFacility, uint64 processingDate) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        product.processingFacility = processingFacility;
        product.dates.processingDate = processingDate;
    }

    function updatePackagingFacility(uint256 productId, string memory packagingFacility, uint64 packagingDate) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        product.packagingFacility = packagingFacility;
        product.dates.packagingDate = packagingDate;
    }

    function updateShipmentLocation(uint256 productId, string memory shipmentLocation, uint64 shipmentDate) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        product.shipmentLocation = shipmentLocation;
        product.dates.shipmentDate = shipmentDate;

    }

    function updateConsumerDetails(uint256 productId, string memory deliveryLocation, uint64 paymentDate, uint256 paymentAmount, uint256 paymentId, address consumerId, string memory paymentStatus) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        product.deliveryLocation = deliveryLocation;
        product.paymentInfo.paymentAmount = paymentAmount;
        product.paymentInfo.paymentId = paymentId;
        product.consumerId = consumerId;
        product.paymentInfo.paymentDate = paymentDate;
        product.paymentInfo.paymentStatus = paymentStatus;
    }

    function productDelivered(uint256 productId) public {
        if (msg.sender != owner) {
            require(isAuthorized(msg.sender), "Sender not authorized to update harvest date");
        }
        productData storage product = products[productId];
        product.deliveryDate = block.timestamp;
    }

    function getProductData(uint256 productId) public view returns (
        string memory,
        string memory,
        string memory,
        string memory,
        string memory,
        uint256,
        address ,
        address
    ) {
        productData storage product = products[productId];
        return (
        product.farmLocation,
        product.processingFacility,
        product.packagingFacility,
        product.shipmentLocation,
        product.deliveryLocation,
        product.deliveryDate,
        product.consumerId,
        product.owner
        );
    }

    function getProductDates(uint256 productId) public view returns (uint64, uint64, uint64, uint64, uint64) {
        ProductDates memory dates = products[productId].dates;
        return (dates.plantingDate, dates.harvestDate, dates.processingDate, dates.packagingDate, dates.shipmentDate);
    }

    function getPaymentInfo(uint256 productId) public view returns (string memory, uint256, uint256, uint256) {
        PaymentInfo memory payInfo = products[productId].paymentInfo;
        return (payInfo.paymentStatus,payInfo.paymentDate,payInfo.paymentAmount,payInfo.paymentId);
    }


    function isAuthorized(address sender) private view returns (bool) {
        uint256 senderUint = uint256(addressToUint(sender));
        for (uint i = 0; i < authorizedAddresses.length; i++) {
            if (authorizedAddresses[i] == senderUint) {
                return true;
            }
        }
        return false;
    }

    function addAuthorizedAddress(address newAddress) public {
        require(msg.sender == owner, "Only the owner can add authorized addresses");
        authorizedAddresses.push(uint256(addressToUint(newAddress)));
    }

    function removeAuthorizedAddress(address addressToRemove) public {
        require(msg.sender == owner, "Only the owner can remove authorized addresses");
        uint256 addressUint = uint256(addressToUint(addressToRemove));
        for (uint i = 0; i < authorizedAddresses.length; i++) {
            if (authorizedAddresses[i] == addressUint) {
                delete authorizedAddresses[i];
                break;
            }
        }
    }

    function addressToUint(address a) private pure returns (uint256 b) {
        b = uint256(uint160(a));
    }
    constructor() {
        owner = msg.sender;
    }

}

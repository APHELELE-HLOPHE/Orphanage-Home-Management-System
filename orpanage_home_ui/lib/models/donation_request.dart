class DonationRequest {
  final String itemName;
  final int quantity;
  final String description;

  DonationRequest({
    required this.itemName,
    required this.quantity,
    required this.description,
  });

  Map<String, dynamic> toJson() {
    return {
      'itemName': itemName,
      'quantity': quantity,
      'description': description,
    };
  }
}
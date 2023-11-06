//
//  ViewController.h
//  Zadanie 4
//
//  Created by student on 30/10/2023.
//  Copyright © 2023 pb. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SecondViewController.h"

@interface ViewController : UIViewController<SecondViewControllerDelegate>

@property (nonatomic, weak) IBOutlet UILabel *messageLabel;
@property (nonatomic, weak) IBOutlet UITextField *inputField;
@property (nonatomic, weak) IBOutlet UITextField *inputSurname;

-(IBAction) enter;
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender;
-(void)viewDidLoad;


@end


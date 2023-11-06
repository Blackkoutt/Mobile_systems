//
//  ViewController.m
//  Zadanie 4
//
//  Created by student on 30/10/2023.
//  Copyright © 2023 pb. All rights reserved.
//

#import "ViewController.h"
#import "SecondViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}
-(IBAction)enter{
    NSString *yourName = self.inputField.text;
    NSString *myName = @"Mateusz";
    NSString *message;
    
    if([yourName length] == 0){
        yourName = @"World";
    }
    
    if([yourName isEqualToString:myName]){
        message = [NSString stringWithFormat:@"Hello %@! We have the same name :)", yourName];
    }
    else{
        message = [NSString stringWithFormat:@"Hello %@!",yourName];
    }
    
    self.messageLabel.text = message;
    
}
-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"sendSurnameSeuge"]){
        SecondViewController *controller = (SecondViewController *) segue.destinationViewController;
        controller.surname=self.inputSurname.text;
        controller.delegate = self;
    }
}
-(void) addItemViewController:(SecondViewController *) controller didFinishEnteringItem:(NSString *) item{
        NSLog(@"This was returned from SecondViewController %@", item);
        self.inputSurname.text = item;
}

@end
